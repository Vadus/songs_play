var STATIC_RES = "assets/";

var Player = function(songlist){
    this.songlist = songlist;
    
    this.ytPlayerId;
    this.ytPlayer;
    this.scWidgetId;
    this.scPlayerId;
    this.scPlayer;
    
    this.currentSong;
    this.running = false;
};

Player.prototype.run = function(songlist){
    if(songlist != undefined){
      this.songlist = songlist;
    }
    this.running = true;
};

Player.prototype.playVideo = function(songId, songKey, songSource){

    console.log("playing video " + songKey)
    
    if(songSource == "YT"){
      this._togglePlayer(this.ytPlayerId);
      this._playVideoYt(songKey);
    }
    else if (songSource == "SC"){
      this._togglePlayer(this.scPlayerId);
      this._playVideoSc(songKey);
    }
    
    this.currentSong = this._getSong(songId);
    this._togglePlayButton(songId, songSource, "Start");
};

Player.prototype.stopVideo = function(songId, songSource){

    console.log("stopping video...");
    if(songSource == "YT"){
      this._stopVideoYt();
    }
    else if (songSource == "SC"){
      this._stopVideoSc();
    }
    this._togglePlayButton(songId, songSource, "Stop");
};

Player.prototype.playNextSong = function(){
    if(this.songlist != undefined){
        console.log("playing next song in songlist..");
        var currentPos = 0;
        if(this._hasCurrentSong()){
            currentPos = this.currentSong.pos;
        }
        var firstSong;
        var nextSong;
        for(var i in this.songlist){
            var song = this.songlist[i];
            if(i == 0){
                firstSong = song;
            }
            var songId = song.id;
            var songpos = song.pos;
            if(songpos != undefined && songpos > currentPos){
                nextSong = song;
                var songKey = nextSong.url;
                var songSource = nextSong.source;
                break;
            }
        }
        if(nextSong != undefined && nextSong != null){
            this.playVideo(nextSong.id, nextSong.url, nextSong.source);
        }
        else{
            this.playVideo(firstSong.id, firstSong.url, firstSong.source);
        }
    }
};

Player.prototype._getSong = function(id){
    for(var i in this.songlist){
        var song = this.songlist[i];
        if(id == song.id){
            return song;
        }
    }
    return null;
};

Player.prototype._hasCurrentSong = function(){
    return this.currentSong != undefined && this.currentSong != null;
};

Player.prototype._togglePlayer = function(playerId){
    for (var i = 0, el; el = document.getElementsByClassName("player")[i]; i++){
      el.style.display = "none";
    }
    document.getElementById(playerId).style.display = "block";
};

Player.prototype._togglePlayButton = function(songId, songSource, startStop){
    var buttonId = songSource.toLowerCase() + "Button" + startStop + "_" + songId;
    document.getElementById(buttonId).style.display = "none";
    
    var otherButtonId = songSource.toLowerCase() + "Button" + (startStop == "Start"? "Stop":"Start") + "_" + songId;
    document.getElementById(otherButtonId).style.display = "inline";
};

Player.prototype.initYoutube = function(playerId){
    this.ytPlayerId = playerId;
    
    this.ytPlayer = new YT.Player(playerId, {
      height: '150',
      width: '300',
      videoId: 'M7lc1UVf-VE',
      events: {
        'onReady': onYtPlayerReady,
        'onStateChange': onYtPlayerStateChange
      }
    });
};

Player.prototype.initSoundcloud = function(playerId, iframeId){

    console.log("Init Soundcloud");
    
    this.scPlayerId = playerId;
    this.scWidgetId = iframeId;
    this.scPlayer   = SC.Widget(this.scWidgetId);
    this.scPlayer.bind(SC.Widget.Events.FINISH, function(player, data){
        console.log('SC finished');
        _onPlayerHasFinished("SC");
    });

    /*
    var scButtonsStart = document.querySelectorAll('.scButtonStart');
    console.log("found " + scButtonsStart.length + " song elements");
    for( var i in scButtonsStart) {
      var scButtonStart = scButtonsStart[i];
      var scUrl = scButtonStart.alt;
      if(scUrl != undefined){
        //console.log("Setting a funtion for img " + scUrl);
        scButtonStart.onclick = function(e){
          var songId = e.target.id.substring("scButtonStart_".length);
          var scUrl = e.target.alt;
          playVideo(songId, scUrl, "SC");
        };
      }
    }

    var scButtonsStop = document.querySelectorAll(".scButtonStop");
    console.log("found " + scButtonsStop.length + " song elements");
    for( var i in scButtonsStop) {
      var scButtonStop = scButtonsStop[i];
      scButtonStop.onclick = function(e){
        var songId = e.target.id.substring("scButtonStop_".length);
        stopVideo(songId, "SC");
      };
    }
    */
};

Player.prototype._playVideoYt = function(videoUrl){
    this.ytPlayer.loadVideoByUrl({'mediaContentUrl': videoUrl});
    this.ytPlayer.playVideo();
};

Player.prototype._stopVideoYt = function(){
    this.ytPlayer.stopVideo();
};

Player.prototype._playVideoSc = function(videoUrl){
    this.scPlayer.load(videoUrl, {
      auto_play: true
    });
};

Player.prototype._stopVideoSc = function(){
    this.scPlayer.pause();
};

var _player;

function initSonglist(songlist){

    // create song list
    var songlistUl = document.getElementById("songlist").getElementsByTagName("ul")[0];
    for(var i in songlist){
        var song = songlist[i];

        var songLi = createSongLi(song);

        songlistUl.appendChild(songLi);
    }
};

function addSong(song){

    _player.songlist.push(song);

    var songlistUl = document.getElementById("songlist").getElementsByTagName("ul")[0];
    var songLi = createSongLi(song);
    songlistUl.appendChild(songLi);
};

function createSongLi(song){
    var songLi = document.createElement("li");
    var songDiv = document.createElement("div");
    songDiv.setAttribute("id", song.id);
    var songDivTop = document.createElement("div");
    var songDivBottom = document.createElement("div");

    var songSpan = document.createElement("span");
    songSpan.innerHTML = song.title;
    var imgStart = document.createElement("img");
    if(song.source == "YT"){
      imgStart.setAttribute("id", "ytButtonStart_" + song.id);
      imgStart.setAttribute("class", "ytButtonStart");
      imgStart.setAttribute("src", STATIC_RES + "icons/yt_play.svg");
    }
    else if(song.source == "SC"){
      imgStart.setAttribute("id", "scButtonStart_" + song.id);
      imgStart.setAttribute("class", "scButtonStart");
      imgStart.setAttribute("src", STATIC_RES + "icons/sc_play.svg");
    }
    imgStart.setAttribute("alt", song.url);
    imgStart.setAttribute("style", "width: 20px; height: 20px;");
    imgStart.setAttribute("onclick", "playVideo('"+song.id+"', '"+song.url+"', '"+song.source+"')");

    var imgStop = document.createElement("img");
    if(song.source == "YT"){
      imgStop.setAttribute("id", "ytButtonStop_" + song.id);
      imgStop.setAttribute("class", "ytButtonStop");
      imgStop.setAttribute("src", STATIC_RES + "icons/yt_pause.svg");
    }
    else if(song.source == "SC"){
      imgStop.setAttribute("id", "scButtonStop_" + song.id);
      imgStop.setAttribute("class", "scButtonStop");
      imgStop.setAttribute("src", STATIC_RES + "icons/sc_pause.svg");
    }
    imgStop.setAttribute("alt", song.url);
    imgStop.setAttribute("style", "width: 20px; height: 20px; display: none;");
    imgStop.setAttribute("onclick", "stopVideo('"+song.id+"', '"+song.source+"')");

    var songLinkDiv = document.createElement("div");
    songLinkDiv.style.float = "left";
    songLinkDiv.style.paddingRight = "5px";
    var songLink = document.createElement("a");
    songLink.setAttribute("href", song.sourceUrl);
    songLink.setAttribute("target", "_blank");
    songLink.innerHTML = "Source";
    /*
    <span>Alice Phoebe Lou - Fiery Heart, Fiery Mind (official video)</span>
    <img id="ytButtonStart_1" class="ytButtonStart" src="http://songs.klarblick.org/static/yt_play.svg" alt="https://www.youtube.com/v/KuAXv5qaSbQ" style="width: 20px; height: 20px;"/>
    <img id="ytButtonStop_1" class="ytButtonStop" src="http://songs.klarblick.org/static/yt_pause.svg" alt="https://www.youtube.com/v/KuAXv5qaSbQ" style="width: 20px; height: 20px; display: none"/>
    <br/><a href="https://www.youtube.com/watch?v=KuAXv5qaSbQ" target="_blank" >Source</a>
    */

    songLinkDiv.appendChild(songLink);

    songDivTop.appendChild(imgStart);
    songDivTop.appendChild(imgStop);
    songDivTop.appendChild(songSpan);

    songDivBottom.appendChild(songLinkDiv);

    var tagDiv = document.createElement("div");
    tagDiv.setAttribute("class", "tagDiv");

    for(var t in song.tags){
        var tag = song.tags[t];
        var tagLink = document.createElement("a");
        tagLink.setAttribute("href", "?tag=" + tag.name);
        tagLink.innerHTML = tag.name;
        tagLink.style.paddingRight = "5px";

        tagDiv.appendChild(tagLink);
    }
    var clearDiv = document.createElement("div");
    clearDiv.style.clear = "both";
    tagDiv.appendChild(clearDiv);

    songDivBottom.appendChild(tagDiv);

    songDiv.appendChild(songDivTop);
    songDiv.appendChild(songDivBottom);

    songLi.appendChild(songDiv);

    return songLi;
};

function playVideo(songId, videoUrl, videoSource){
    _player.playVideo(songId, videoUrl, videoSource)
};

function stopVideo(songId, videoSource){
    _player.stopVideo(songId, videoSource);
};

function initPlayer(songlist){
    
    initSonglist(songlist);
    
    _player = new Player(songlist);
    _player.initSoundcloud("sc_player", "sc-widget");
    
    // 2. This code loads the IFrame Player API code asynchronously.
    var tag = document.createElement('script');
    
    tag.src = "https://www.youtube.com/iframe_api";
    var firstScriptTag = document.getElementsByTagName('script')[0];
    firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);

};

// 3. This function creates an <iframe> (and YouTube player)
//    after the API code downloads.
function onYouTubeIframeAPIReady() {
    console.log("Init Youtube");
    _player.initYoutube("yt_player");
    
    _player.run();
};

function onYtPlayerReady(){

    /*
    var ytButtonsStart = document.querySelectorAll('.ytButtonStart');
    for(var i in ytButtonsStart) {
      var ytButtonStart = ytButtonsStart[i];
      var songUrl = ytButtonStart.alt;
      if(songUrl != undefined){
        ytButtonStart.onclick = function(e){
          var songId = e.target.id.substring("ytButtonStart_".length);
          var videoUrl = e.target.alt;
          console.log("Loading video "+ videoUrl);
          playVideo(songId, videoUrl, "YT");
        }
      }
    };

    var ytButtonsStop = document.querySelectorAll(".ytButtonStop");
    for(var i in ytButtonsStop) {
      var ytButtonStop = ytButtonsStop[i];
      ytButtonStop.onclick = function(e){
        var songId = e.target.id.substring("ytButtonStop_".length);
        console.log("Stopping video");
        stopVideo(songId, "YT");
      }
    };
    */
};

function onYtPlayerStateChange(e){
    console.log('YT Player State is:', e.data);
    var ytPlayerState = e.data;
    if(e.data == "0"){ //player has ended a video
      _onPlayerHasFinished("YT")
    }
};

function _onPlayerHasFinished(source){
    if(_player.running && _player._hasCurrentSong()){
        _player.stopVideo(_player.currentSong.id, source)
        _player.playNextSong();
    }
};