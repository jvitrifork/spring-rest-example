//  Copyright (c) 2018 Surevine Ltd.
//
//  Permission is hereby granted, free of charge, to any person
//  obtaining a copy of this software and associated documentation
//  files (the "Software"), to deal in the Software without
//  restriction, including without limitation the rights to use, copy,
//  modify, merge, publish, distribute, sublicense, and/or sell copies
//  of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions:
//
//  The above copyright notice and this permission notice shall be
//  included in all copies or substantial portions of the Software.
//  
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
//  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
//  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
//  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
//  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
//  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
//  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
//  OTHER DEALINGS IN THE SOFTWARE.

package com.surevine.springrestexample.oidc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surevine.springrestexample.entity.User;
import com.surevine.springrestexample.repository.UserRepository;

@Component
public class SavePrincipalAuthenticationSuccessListener {
	private static final Logger LOG = LoggerFactory.getLogger(SavePrincipalAuthenticationSuccessListener.class);

	private final UserRepository userRepository;

	public SavePrincipalAuthenticationSuccessListener(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@EventListener
	@Transactional
	public void onAuthenticationSuccess(InteractiveAuthenticationSuccessEvent event) {
		Authentication authentication = event.getAuthentication();
		if (!userRepository.existsByPrincipalId(authentication.getName())) {
			LOG.debug("Saving user: {}", authentication.getName());
			userRepository.save(new User(authentication.getName()));
		}
	}
}