package xyz.imaginehave.sprouth.service;


import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import xyz.imaginehave.sprouth.entity.SprouthUser;
import xyz.imaginehave.sprouth.entity.SprouthVerificationToken;
import xyz.imaginehave.sprouth.repository.SprouthVerficationTokenRepository;

@Service
@Slf4j
public class SprouthVerificationTokenService {
	
	@Autowired
	SprouthVerficationTokenRepository sprouthVerficationTokenRepository;
	
	private String getHash(String toHash) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
		    md.update(toHash.getBytes(StandardCharsets.UTF_8));
		    byte[] digest = md.digest();
			return String.format("%064x", new BigInteger(1, digest));
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage());
			return null;
		}
	}

	
	public void createToken(SprouthUser user) {
		SprouthVerificationToken sprouthVerificationToken = new SprouthVerificationToken(user);
		sprouthVerificationToken.setToken(getHash(user.getUsername()));
		sprouthVerficationTokenRepository.save(sprouthVerificationToken);
	}

}
