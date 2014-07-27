package org.smigo.user;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Locale;

public class UserBean implements UserDetails, User {

    private static final Logger log = LoggerFactory.getLogger(UserBean.class);

    private int id = 0;

    @SafeHtml(whitelistType = WhiteListType.NONE)
    private String displayname = "";

    @Email
    private String email = "";

    @Username
    @Size(min = 5, max = 40)
    @Pattern(regexp = "\\w*")
    private String username = "";

    @NewPassword
    private String password = "";

    @SafeHtml(whitelistType = WhiteListType.BASIC_WITH_IMAGES)
    private String about = "";

    private Locale locale = Locale.ENGLISH;

    @AssertTrue
    private boolean termsofservice;

    public UserBean() {
    }


    public UserBean(int id, String username, String password, String displayname, String email, String about, Locale locale) {
        this.id = id;
        this.displayname = displayname;
        this.password = password;
        this.username = username;
        this.email = email;
        this.about = about;
        this.locale = locale;
        log.debug("Create user instance " + this.toString());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", displayname='" + displayname + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String firstname) {
        this.displayname = firstname;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public java.util.Collection<? extends GrantedAuthority> getAuthorities() {
        return java.util.Collections.singleton(new SimpleGrantedAuthority("user"));
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isTermsofservice() {
        return termsofservice;
    }

    public void setTermsofservice(boolean termsofservice) {
        this.termsofservice = termsofservice;
    }

    @Override
    public boolean isAuthenticated() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
    }
}