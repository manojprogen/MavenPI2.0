/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.users;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

/**
 *
 * @author sreekanth
 */
public class ProgenUser implements HttpSessionBindingListener {

    public static Map<ProgenUser, HttpSession> logins = new HashMap<ProgenUser, HttpSession>();
    private static HashSet<Integer> duplicateLogins = new HashSet<Integer>();
    public Integer userId;
    private boolean removeWhenUnbound = true;

    public ProgenUser(int pu_id) {
        userId = pu_id;
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof ProgenUser) && (userId != null) ? userId.equals(((ProgenUser) other).userId) : (other == this);
    }

    @Override
    public int hashCode() {
        return (userId != null) ? (this.getClass().hashCode() + userId.hashCode()) : super.hashCode();
    }

    @Override
    public String toString() {
        return userId.toString();
    }

    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        //HttpSession session = logins.remove(this);
        HttpSession session = logins.get(this);
        if (session != null) {
            synchronized (ProgenUser.class) {
                duplicateLogins.add(this.userId);
            }
        } else {
            synchronized (ProgenUser.class) {
                logins.put(this, event.getSession());
            }
        }
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        if (this.removeWhenUnbound) {
            synchronized (ProgenUser.class) {
                logins.remove(this);
            }
        }

    }

    public boolean isDuplicateSession(Integer userId) {
//       if ( duplicateLogins.contains(userId))
//           return true;
//       else
        return false;

    }

    public Integer getUserId() {
        return this.userId;
    }

    public void killAndSetNewSession(ProgenUser user, HttpSession newUserSession) {
        synchronized (ProgenUser.class) {
            duplicateLogins.remove(user.getUserId());
            HttpSession session = logins.remove(user);
            session.invalidate();
            logins.put(user, newUserSession);
        }
    }

    public void removeDuplicateUser(Integer userId) {
        synchronized (ProgenUser.class) {
            duplicateLogins.remove(userId);
        }
        this.removeWhenUnbound = false;
    }
}
