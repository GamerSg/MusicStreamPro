package sg.edu.tp.musicstream;

import java.util.Random;

//SongCollection Singleton Class
public class SongCollection
{
    //Sole instance of SongCollection
    private static SongCollection scSingleton = null;
    private Song[] songs = new Song[8];
    private boolean filterOn = false;
    //Get SongCollection Singleton instance
    public static SongCollection getSongCollection()
    {
        if (scSingleton == null)
        {
            scSingleton = new SongCollection();
        }
        return scSingleton;
    }
    //Prevent public creation of SongCollection
    private SongCollection()
    {
        prepareSongs();
    }

    public Song searchById(String id)
    {
        //For-each loop simplifies for loop syntax
        //Loop through songs array, current indexed song will be in s
        for (Song s: songs )
        {
            if(s.getId().equals(id))
            {
                return s;  //If id is found, exit with the song
            }
        }
        //If we reach this point, no song was found, return null
        return null;
    }

    public Song getNextSong(String currentSongId)
    {
        //Create temp song
        Song song = null;
        //Find current song in list
        for(int i = 0; i < songs.length; ++i)
        {
            Song s = songs[i];
            if(s.getId().equals(currentSongId) && i < songs.length-1)
            {//Found current song, return next song
                song = songs[i+1];
                break;
            }

        }
        if(song == null)
        {
            song = songs[0];
        }
        return song;
    }

    public Song getNextShuffleSong(String currentSongId)
    {
        //Figure out current song
        Song currentSong = searchById(currentSongId);

        Random rand = new Random(); //instance of random class
        Song next = null;
        //Find a random next song, make sure it is not the same as the current song
        do
        {
            int int_random = rand.nextInt(songs.length);
            next = getSong(int_random);
        }while(next == currentSong);    //Try again if next song is the same as current song

        return next;
    }


    public int getNumSongs()
    {
        return songs.length;
    }

    public Song getSong(int pos)
    {
        return songs[pos];
    }

    public Song getPrevSong(String currentSongId) {
        //Create temp song
        Song song = null;
        //Find current song in list
        for (int i = 0; i < songs.length; ++i) {
            Song s = songs[i];
            if (s.getId().equals(currentSongId) && i > 0) {//Found current song, return next song
                song = songs[i - 1];
                break;
            }
        }
        if (song == null)
        {
            song = songs[songs.length -1];
        }
        return song;
    }

    private void prepareSongs() {
        Song byeBye = new Song("S1001",
                "Bye Bye Bye", "NSync",
                "72b91cb27fac7beb50a2deffdd8b821fba5ba950?cid=2afe87a64b0042dabf51f37318616965",
                4.9, "nsync");

        Song jumme = new Song("S1002",
                "Jumme Ki Raat",
                "Mika Singh",
                "f7768ce082f47c1401459dddc29718f4af59ca6f?cid=2afe87a64b0042dabf51f37318616965",
                4.56,
                "kick");

        Song qingTian = new Song("S1003",
                "晴天", "Jay Chou",
                "ebfcc82ed984f5ee49faef09c5fc95d5c4c6486f?cid=2afe87a64b0042dabf51f37318616965",
                4.5, "jayc");

        Song everybody = new Song("S1004",
                "Everybody", "Backstreet Boys",
                "2da7ea19b35ecbfaf2dd7273e9b305a4e090bbc9?cid=2afe87a64b0042dabf51f37318616965",
                4.5, "backstreetboys");

        Song moments = new Song("S1005",
                "Moments", "Ayumi Hamasaki",
                "10f784df444e6a66f8671ce18f968f5d5f64841d?cid=2afe87a64b0042dabf51f37318616965",
                4.5, "ayumi");

        Song qili = new Song("S1006",
                "七里香", "Jay Chou",
                "25e19ff5fb10014d49f1a80be3637e2635b6a058?cid=2afe87a64b0042dabf51f37318616965",
                4.5, "jayc2");

        Song ooj = new Song("S1007",
                "O O Jaane Jaana", "Kamaal Khan",
                "1d4d63740e0abf893f0a276b0af1b304aa9fefad?cid=2afe87a64b0042dabf51f37318616965",
                4.5, "pktdk");

        Song wantit = new Song("S1008",
                "I Want It That Way", "Backstreet Boys",
                "b8c2410a5acb68b462be6ac85f1312430e2b149c?cid=2afe87a64b0042dabf51f37318616965",
                4.5, "millenium");




        songs[0] = byeBye;
        songs[1] = jumme;
        songs[2] = qingTian;
        songs[3] = everybody;
        songs[4] = moments;
        songs[5] = qili;
        songs[6] = ooj;
        songs[7] = wantit;
    }
}
