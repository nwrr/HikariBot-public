package HikariBot;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class PlayerManager {
    private static PlayerManager INSTANCE;
    
    private final Map<Long, GuildMusicManager> musicMap;
    private final AudioPlayerManager audioPlayerManager;
    
    public PlayerManager() {
        this.musicMap = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        
        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }
    
    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicMap.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            return guildMusicManager; //To change body of generated lambdas, choose Tools | Templates.
        });
    }
    
    public void loadAndPlay(TextChannel channel, String trackUrl, SlashCommandEvent event) {
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());
        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                musicManager.scheduler.queue(track);
                if(event == null) return;
                event.reply("\uD83C\uDFB6 Memutar : `" + track.getInfo().title + "` oleh `" + track.getInfo().author + "`").queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (trackUrl.contains("ytsearch:")) {
                    AudioTrack track = playlist.getSelectedTrack();
                    if (track == null) track = playlist.getTracks().get(0);
                    musicManager.scheduler.queue(track);
                    if(event == null) return;
                    event.reply("\uD83C\uDFB6 Memutar : `" + track.getInfo().title + "` oleh `" + track.getInfo().author + "`").queue();
                } else {
                    List<AudioTrack> track = playlist.getTracks();
                    for(AudioTrack tracks : track) {
                        musicManager.scheduler.queue(tracks);
                    }
                    if(event == null) return;
                    event.reply("\uD83C\uDFB6 Memutar : `" + track.size() + " lagu` dari `" + playlist.getName() + "`").queue();
                }
            }

            @Override
            public void noMatches() {
                event.reply("Tidak Ditemukan " + trackUrl).setEphemeral(true).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                event.reply("Tidak dapat diputar karena : " + exception.getMessage()).queue();
            }
        });
    }
    
    public static PlayerManager getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }
    
}
