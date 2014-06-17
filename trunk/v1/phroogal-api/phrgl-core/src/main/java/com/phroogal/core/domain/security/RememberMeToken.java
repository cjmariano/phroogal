package com.phroogal.core.domain.security;

import java.io.Serializable;
import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

import com.phroogal.core.domain.Persistent;

@Document(collection = "logintokens")
public class RememberMeToken implements Persistent<ObjectId>, Serializable{

	private static final long serialVersionUID = -1448492241233258853L;

	@Id
	private ObjectId id;
	
	@Indexed
	private String username;
	
	@Indexed
    private String series;
     
    private String tokenValue;
     
    private Date date;
    
    public RememberMeToken(){
        
    }
     
    public RememberMeToken(PersistentRememberMeToken token){
        this.series = token.getSeries();
        this.username = token.getUsername();
        this.tokenValue = token.getTokenValue();
        this.date = token.getDate();
    }
	
	@Override
	public ObjectId getId() {
		return id;
	}

	@Override
	public void setId(ObjectId id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getTokenValue() {
		return tokenValue;
	}

	public void setTokenValue(String tokenValue) {
		this.tokenValue = tokenValue;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
