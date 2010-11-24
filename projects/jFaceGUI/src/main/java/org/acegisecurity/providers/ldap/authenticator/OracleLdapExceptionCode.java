package org.acegisecurity.providers.ldap.authenticator;

import commons.security.exception.AccountLockedException;
import commons.security.exception.AuthenticationException;
import commons.security.exception.BadPasswordCompositionException;
import commons.security.exception.DisabledAccountException;
import commons.security.exception.ExpiredPasswordException;
import commons.security.exception.PasswordAboutToExpireException;

/**
 * Representa los tipos de excepción que puede arrojar el LDAP de Oracle.
 */
public enum OracleLdapExceptionCode {

	/**
	 * 9000 GSL_PWDEXPIRED_EXCP Your Password has expired. Please contact the Administrator to change your password.
	 */
	CODE_9000() {
		@Override
		public AuthenticationException getAuthenticationException(Throwable cause) {
			return new ExpiredPasswordException("Su contraseña ha expirado. Por favor, cambie su contraseña.", cause);
		}
	},
	/**
	 * 9001 GSL_ACCOUNTLOCKED_EXCP Your account is locked. Please contact the Administrator.
	 */
	CODE_9001() {
		@Override
		public AuthenticationException getAuthenticationException(Throwable cause) {
			return new AccountLockedException("Su cuenta está bloqueada. Contacte al administrador.", cause);
		}
	},
	/**
	 * 9002 GSL_EXPIREWARNING_EXCP Your Password will expire in pwdexpirewarning seconds. Please change your password
	 * now.
	 */
	CODE_9002() {
		@Override
		public AuthenticationException getAuthenticationException(Throwable cause) {

			return new PasswordAboutToExpireException("Su contraseña va a expirar en breve. Cambie su contraseña.",
					cause);
		}
	},
	/**
	 * 9003 GSL_PWDMINLENGTH_EXCP Your Password must be at least pwdminlength characters long.
	 */
	CODE_9003() {
		@Override
		public AuthenticationException getAuthenticationException(Throwable cause) {
			return new BadPasswordCompositionException("Su contraseña no posee la suficiente cantidad de caracteres.",
					cause);
		}
	},
	/**
	 * 9004 GSL_PWDNUMERIC_EXCP Your Password must contain at least orclpwdalphanumeric numeric characters.
	 */
	CODE_9004() {
		@Override
		public AuthenticationException getAuthenticationException(Throwable cause) {
			return new BadPasswordCompositionException(
					"Su contraseña no contiene la suficiente cantidad de caracteres numéricos.", cause);
		}
	},
	/**
	 * 9005 GSL_PWDNULL_EXCP Your Password cannot be a Null Password.
	 */
	CODE_9005() {
		@Override
		public AuthenticationException getAuthenticationException(Throwable cause) {
			return new BadPasswordCompositionException("Su contraseña no puede ser nula.", cause);
		}
	},
	/**
	 * 9006 GSL_PWDINHISTORY_EXCP Your New Password cannot be the same as your Old Password.
	 */
	CODE_9006() {
		@Override
		public AuthenticationException getAuthenticationException(Throwable cause) {
			return new BadPasswordCompositionException("Su nueva contraseña no puede ser igual a la anterior.", cause);
		}
	},
	/**
	 * 9007 GSL_PWDILLEGALVALUE_EXCP Your Password cannot be the same as your orclpwdillegalvalues.
	 */
	CODE_9007() {
		@Override
		public AuthenticationException getAuthenticationException(Throwable cause) {
			return new BadPasswordCompositionException(
					"Su nueva contraseña no puede ser igual a otras utilizadas con anterioridad.", cause);
		}
	},
	/**
	 * 9008 GSL_GRACELOGIN_EXCP Your Password has expired. You have pwdgraceloginlimit Grace logins left.
	 */
	CODE_9008() {
		@Override
		public AuthenticationException getAuthenticationException(Throwable cause) {
			return new ExpiredPasswordException("Su contraseña ha expirado. Por favor, cambie su contraseña.", cause);
		}
	},
	/**
	 * 9050 GSL_ACCTDISABLED_EXCP Your Account has been disabled. Please contact the administrator.
	 */
	CODE_9050() {
		@Override
		public AuthenticationException getAuthenticationException(Throwable cause) {
			return new DisabledAccountException("Su cuenta ha sido deshabilitada. Contacte al administrador.", cause);
		}
	};

	public abstract AuthenticationException getAuthenticationException(Throwable cause);
}
