using Microsoft.AspNetCore.Diagnostics;
using Microsoft.AspNetCore.Mvc;
using NotificationManager.Dto.Shared;

namespace NotificationManager.Filters;

public class GlobalExceptionHandler : IExceptionHandler
{
  private readonly ILogger<GlobalExceptionHandler> _logger;

  public GlobalExceptionHandler(ILogger<GlobalExceptionHandler> logger)
  {
    _logger = logger;
  }

  public async ValueTask<bool> TryHandleAsync(
      HttpContext httpContext,
      Exception exception,
      CancellationToken cancellationToken)
  {
    // Case 1: Handle Bad Requests / Model Binding Validation Failures
    if (exception is BadHttpRequestException || exception is ArgumentException)
    {
      httpContext.Response.StatusCode = StatusCodes.Status400BadRequest;

      var validationProblem = new ProblemDetails
      {
        Status = StatusCodes.Status400BadRequest,
        Title = "Validation Error",
        Detail = exception.Message
      };

      await httpContext.Response.WriteAsJsonAsync(validationProblem, cancellationToken);
      return true;
    }

    // Case 2: Unhandled/Unexpected generic faults (Internal Server Error)
    string traceId = Guid.NewGuid().ToString();
    _logger.LogError(exception, "Unhandled exception. traceId={TraceId}", traceId);

    httpContext.Response.StatusCode = StatusCodes.Status500InternalServerError;

    var errorResponse = new GenericErrorResponse(
        $"An unexpected error occurred. Please contact support with traceId: {traceId}"
    );

    await httpContext.Response.WriteAsJsonAsync(errorResponse, cancellationToken);

    return true;
  }
}
