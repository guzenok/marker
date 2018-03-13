package marker;


import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

@Entity // This tells Hibernate to make a table out of this class
@Table( uniqueConstraints = {
		@UniqueConstraint( columnNames = {"SHEET_ID","AUTHOR_ID", "DELETED"} ) } )
public class Mark {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="SHEET_ID",nullable=false)
	private Sheet sheet;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="AUTHOR_ID",nullable=false)
    private User author;
	
    @Column(nullable=false)
    private Integer value;

    private String descr;
    
    @Column(nullable=false, columnDefinition="TIMESTAMP default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date dt;
    
    @Column(nullable=true)
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date deleted;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAuthorId() {
		return this.author.getId();
	}

	public void setAuthorId(Long user_id) {
		if( this.author == null ){
			this.author = new User();
		}
		this.author.setId(user_id);
	}

	public Long getSheetId() {
		return this.sheet.getId();
	}

	public void setSheetId(Long sheet_id) {
		if( this.sheet == null ){
			this.sheet = new Sheet();
		}
		this.sheet.setId(sheet_id);
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User user) {
		this.author = user;
	}
	
	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}
	
	public Date getDt() {
		return dt;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}
	
	public Date getDeleted() {
		return this.deleted;
	}

	public void setDeleted(Date dt) {
		this.deleted = dt;
	}
	
}

