package data

type AuditAction string

const (
	// Standard CRUD
	ActionCreate AuditAction = "CREATE"
	ActionRead   AuditAction = "READ"
	ActionUpdate AuditAction = "UPDATE"
	ActionDelete AuditAction = "DELETE"

	// Registration & Workflow
	ActionApprove  AuditAction = "APPROVE"
	ActionReject   AuditAction = "REJECT"
	ActionCancel   AuditAction = "CANCEL"
	ActionRestore  AuditAction = "RESTORE"

	// Security & Access
	ActionRegSuccess    AuditAction = "REGISTRATION_SUCCESS"
	ActionRegFailure    AuditAction = "REGISTRATON_FAILURE"
	ActionLoginSuccess  AuditAction = "LOGIN_SUCCESS"
	ActionLoginFailure  AuditAction = "LOGIN_FAILURE"
	ActionLogout        AuditAction = "LOGOUT"
	ActionPermChange    AuditAction = "PERMISSION_CHANGE"
	ActionAccessDenied  AuditAction = "ACCESS_DENIED"

	// System & Admin
	ActionConfigChange     AuditAction = "CONFIG_CHANGE"
	ActionExport           AuditAction = "EXPORT"
	ActionSystemJobExecute AuditAction = "SYSTEM_JOB_EXECUTION"
)