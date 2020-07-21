The URL paths provided by the framework are /oauth/authorize (the authorization endpoint), 
/oauth/token (the token endpoint), 
/oauth/confirm_access (user posts approval for grants here), 
/oauth/error (used to render errors in the authorization server), 
/oauth/check_token (used by Resource Servers to decode access tokens), 
and /oauth/token_key (exposes public key for token verification if using JWT tokens).