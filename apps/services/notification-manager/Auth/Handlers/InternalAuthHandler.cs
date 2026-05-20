using System.Security.Claims;
using System.Text.Encodings.Web;
using Microsoft.AspNetCore.Authentication;
using Microsoft.Extensions.Options;
using NotificationManager.Auth.Client;

namespace NotificationManager.Auth.Handlers;

public class InternalAuthHandler : AuthenticationHandler<AuthenticationSchemeOptions>
{
  private readonly IIamClient _iamClient;
  private readonly ILogger<InternalAuthHandler> _logger;

  public InternalAuthHandler(
      IOptionsMonitor<AuthenticationSchemeOptions> options,
      ILoggerFactory loggerFactory,
      UrlEncoder encoder,
      IIamClient iamClient,
      ILogger<InternalAuthHandler> logger)
      : base(options, loggerFactory, encoder)
  {
    _iamClient = iamClient;
    _logger = logger;
  }

  protected override async Task<AuthenticateResult> HandleAuthenticateAsync()
  {
    // 1. Intercept Authorization Header (Extracting Bearer)
    if (!Request.Headers.TryGetValue("Authorization", out var authHeaderValues))
    {
      return AuthenticateResult.NoResult();
    }

    string? authHeader = authHeaderValues.ToString();
    if (string.IsNullOrWhiteSpace(authHeader) || !authHeader.StartsWith("Bearer ", StringComparison.OrdinalIgnoreCase))
    {
      return AuthenticateResult.NoResult();
    }

    try
    {
      // 2. RPC validation call straight over to auth-manager via discovery mesh
      var response = await _iamClient.ValidateAsync(authHeader);

      if (!response.IsSuccessStatusCode || response.Content is null)
      {
        _logger.LogError("Token validation rejected by auth-manager. Status Code: {Status}", response.StatusCode);
        return AuthenticateResult.Fail("Token validation failed");
      }

      var userPayload = response.Content;

      // 3. Hydrate .NET Security Context Claims
      var claims = new List<Claim>
            {
                new(ClaimTypes.NameIdentifier, userPayload.UserId),
                new(ClaimTypes.Role, userPayload.UserRole) // Automatically matches standard [Authorize(Roles = "...")] filters
            };

      var identity = new ClaimsIdentity(claims, Scheme.Name);
      var principal = new ClaimsPrincipal(identity);
      var ticket = new AuthenticationTicket(principal, Scheme.Name);

      _logger.LogInformation("Successfully authenticated User: {UserId} with Role: {Role}", userPayload.UserId, userPayload.UserRole);
      return AuthenticateResult.Success(ticket);
    }
    catch (Exception ex)
    {
      _logger.LogCritical(ex, "IAM Security cluster fallback occurred: Auth Service Unavailable");
      return AuthenticateResult.Fail("Authentication service unavailable");
    }
  }
}
