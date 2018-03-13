package marker;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;


@Entity // This tells Hibernate to make a table out of this class
public class Sheet {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(nullable=false)
    private String name;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="AUTHOR_ID",nullable=false)
    private User author;   
    
    @OneToMany(mappedBy="sheet",fetch=FetchType.EAGER,orphanRemoval=true)
    public List<Mark> marks;
    
    public void addMark(Mark m) {
        this.marks.add(m);
        if (m.getSheetId() != this.id) {
            m.setSheetId(this.id);
        }
    }

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

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User user) {
		this.author = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

