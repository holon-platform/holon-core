/*
 * Copyright 2016-2017 Axioma srl.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.holonplatform.core.examples;

import java.util.Arrays;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.holonplatform.auth.AuthContext;
import com.holonplatform.auth.Authentication;
import com.holonplatform.spring.security.SpringSecurity;

@SuppressWarnings("unused")
public class ExampleSpringSecurity {

	public void authContext1() {
		// tag::authcontext1[]
		AuthContext authContext = SpringSecurity.authContext(); // <1>

		UsernamePasswordAuthenticationToken tkn = new UsernamePasswordAuthenticationToken("user", "pwd",
				Arrays.asList(new GrantedAuthority[] { new SimpleGrantedAuthority("role1") }));
		SecurityContextHolder.getContext().setAuthentication(tkn); // <2>

		Authentication authc = authContext.requireAuthentication(); // <3>

		String name = authc.getName(); // <4>

		boolean permitted = authContext.isPermitted("role1"); // <5>

		SecurityContextHolder.getContext().setAuthentication(null); // <6>
		boolean notAnymore = authContext.isAuthenticated();
		// end::authcontext1[]
	}

}
