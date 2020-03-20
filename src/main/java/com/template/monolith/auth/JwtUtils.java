package com.template.monolith.auth;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JwtUtils {

	@Autowired
	JwtProperties jwtProp;

	@Autowired
	ObjectMapper objectMapper;
	
	@Value("${jwt.expire.in}")
	private Long expiration;

	private KeyPair getKeyPair() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException,
			UnrecoverableKeyException {
		KeyStore keyStore = KeyStore.getInstance("pkcs12");
		keyStore.load(jwtProp.getKeyStore().getInputStream(), jwtProp.getKeyStorePassword().toCharArray());
		Certificate cert = keyStore.getCertificate(jwtProp.getKeyPairAlias());
		PublicKey publicKey = cert.getPublicKey();
		PrivateKey privateKey = (PrivateKey) keyStore.getKey(jwtProp.getKeyPairAlias(),
				jwtProp.getKeyPairPassword().toCharArray());
		KeyPair keyPair = new KeyPair(publicKey, privateKey);
		return keyPair;
	}

	public String generateToken(String username) throws UnrecoverableKeyException, KeyStoreException,
			NoSuchAlgorithmException, CertificateException, IOException {
		PrivateKey privateKey = getKeyPair().getPrivate();
		RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) privateKey;
		RsaSigner signer = new RsaSigner(rsaPrivateKey);
		JwtContent jwtContent = new JwtContent();
		jwtContent.setUsername(username);
		String exp = (System.currentTimeMillis() / 1000L + expiration) + "";
		jwtContent.setExpiration(exp);
		String content = objectMapper.writeValueAsString(jwtContent);
		Jwt jwt = JwtHelper.encode(content, signer, new HashMap<>());
		return jwt.getEncoded();
	}

	public JwtContent getJwtContent(String token) throws JsonMappingException, JsonProcessingException {
		Jwt jwt = JwtHelper.decode(token);
		String content = jwt.getClaims();
		JwtContent jwtContent = objectMapper.readValue(content, JwtContent.class);
		return jwtContent;
	}

	public String getUsername(String token) throws JsonMappingException, JsonProcessingException {
		JwtContent jwtContent = getJwtContent(token);
		return jwtContent.getUsername();
	}

	public String getExpiration(String token) throws JsonMappingException, JsonProcessingException {
		JwtContent jwtContent = getJwtContent(token);
		return jwtContent.getExpiration();
	}
	
	public Boolean tokenExpired(String token)
			throws NumberFormatException, JsonMappingException, JsonProcessingException {
		Long expiration = new Long(getExpiration(token));
		expiration = expiration * 1000L + 999L;
		return System.currentTimeMillis() > expiration;
	}

}
