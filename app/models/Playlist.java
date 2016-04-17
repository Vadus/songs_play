package models;

import javax.persistence.*;
import java.util.List;

/**
 * Created by DTramnitzke on 10.04.2016.
 */
@Entity
@Table(name = "playlists")
public class Playlist extends AppModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID")
    public Long id;

    public String name;

    @ManyToOne
    public User user;

    @ManyToMany
    @JoinTable(
            name="playlist_songs",
            joinColumns = @JoinColumn(name="PLAYLIST_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "SONG_ID", referencedColumnName = "ID")
    )
    public List<Song> songs;
}