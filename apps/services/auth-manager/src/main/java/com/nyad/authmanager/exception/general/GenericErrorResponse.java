package com.nyad.authmanager.exception.general;

public record GenericErrorResponse(
  String error
)implements ResponseInterface
{}
