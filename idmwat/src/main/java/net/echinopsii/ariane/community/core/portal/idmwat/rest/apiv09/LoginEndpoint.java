/**
 * IDM Web Application Tooling
 * provide a REST tools
 *
 * Copyright (C) 2018 echinopsii
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.echinopsii.ariane.community.core.portal.idmwat.rest.apiv09;

import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.Path;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.echinopsii.ariane.community.core.idm.base.json.LoginJSON;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;

/**
 *
 */
@Path("/api/v09/login")
public class LoginEndpoint {
    private static final Logger log = LoggerFactory.getLogger(LoginEndpoint.class);

    @POST
    public Response Login(@QueryParam("payload") String payload) {
        SignatureAlgorithm sigAlg = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("secret");
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, sigAlg.getJcaName());

        Subject subject = SecurityUtils.getSubject();
        Response ret;

        try {
            LoginJSON.JSONFriendlyLogin loginRequest = LoginJSON.JSON2Login(payload);
            JwtBuilder builder = Jwts.builder()
                    .setSubject(loginRequest.getUsername())
                    .signWith(sigAlg, signingKey);

            UsernamePasswordToken token = new UsernamePasswordToken(loginRequest.getUsername(), loginRequest.getPassword());
            subject.login(token);
            String result = builder.compact();
            ret = Response.status(Status.OK).entity(result).build();
        } catch (Exception e) {
            ret = Response.status(Status.OK).entity("Login Error").build();
        }

        log.debug("Is authenticated:{} ; Is remembered:{}", new Object[]{subject.isAuthenticated(), subject.isRemembered()});
        log.debug("Principal:{}", new Object[]{(subject.getPrincipal() != null) ? subject.getPrincipal().toString() : "guest"});
        return ret;
    }
}