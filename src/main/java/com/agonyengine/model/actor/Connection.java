package com.agonyengine.model.actor;

import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Entity
public class Connection {
    @Id
    @GeneratedValue
    @Type(type = "pg-uuid")
    private UUID id;

    private String account;
    private String sessionUsername;
    private String sessionId;
    private String remoteIpAddress;
    private Date disconnectedDate = null;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getSessionUsername() {
        return sessionUsername;
    }

    public void setSessionUsername(String sessionUsername) {
        this.sessionUsername = sessionUsername;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getRemoteIpAddress() {
        return remoteIpAddress;
    }

    public void setRemoteIpAddress(String remoteIpAddress) {
        this.remoteIpAddress = remoteIpAddress;
    }

    public Date getDisconnectedDate() {
        return disconnectedDate;
    }

    public void setDisconnectedDate(Date disconnectedDate) {
        this.disconnectedDate = disconnectedDate;
    }
}
