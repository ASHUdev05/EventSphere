using NotificationManager.Auth.Dto;
using Refit;

namespace NotificationManager.Auth.Client;

public interface IIamClient
{
  [Get("/auth/validate")]
  Task<IApiResponse<ValidateResponse>> ValidateAsync([Header("Authorization")] string authHeader);
}
