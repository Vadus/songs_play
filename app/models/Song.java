package models;

import play.api.Play;

import javax.persistence.*;
import java.util.List;

/**
 * Created by DTramnitzke on 10.04.2016.
 */
@Entity
@Table(name = "songs")
public class Song extends AppModel{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="ID")
    public Long id;

    public String title;

    public String link;

    //@ManyToMany(mappedBy = "songs")
    //public List<Playlist> playlists;
}
