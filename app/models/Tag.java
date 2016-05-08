package models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

/**
 * Created by DTramnitzke on 08.05.2016.
 */
@Entity
@Table(name = "tags")
public class Tag extends AppModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="ID")
    public Long id;

    public String name;

    @ManyToMany(mappedBy = "tags")
    @JsonBackReference
    public List<Song> songs;

    @ManyToMany(mappedBy = "tags")
    @JsonBackReference
    public List<Playlist> playlists;
}
