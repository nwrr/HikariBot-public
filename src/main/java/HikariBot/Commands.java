package HikariBot;

import java.awt.Color;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import net.dv8tion.jda.api.managers.AudioManager;

/**
 *
 * @author BOOM
 */
public class Commands extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        String[] content = event.getMessage().getContentRaw().split(" ");
        if(content[0].equalsIgnoreCase(Main.prefix + "help")) {
            event.getChannel().sendMessageEmbeds(new EmbedBuilder().setColor(Color.red).addField("Lah kok !!","Move on bego, pake slash command skarang. Ketik '/' di message !", false).build()).queue();
        }
        
        if(content[0].equalsIgnoreCase(Main.prefix + "autoplay")) {
            AudioManager audioManager = event.getGuild().getAudioManager();
            VoiceChannel voiceChannel = event.getMember().getVoiceState().getChannel();
            VoiceChannel botChannel = event.getGuild().getSelfMember().getVoiceState().getChannel();
            if(voiceChannel == null) {
                event.getTextChannel().sendMessage("Lu masuk dlu sono, ntar gw nyusul").queue();
            } else if(!voiceChannel.equals(botChannel) && botChannel != null) {
                event.getTextChannel().sendMessage("Kita berada di dunia yg berbeda").queue();
            } else {
                audioManager.openAudioConnection(voiceChannel);
                PlayerManager.getInstance().loadAndPlay(event.getTextChannel(), "https://www.youtube.com/playlist?list=PL3oW2tjiIxvSk0WKXaEiDY78KKbKghOOo", null);
            }
        }        
    }
    
    @Override
    public void onSlashCommand(SlashCommandEvent event) throws NullPointerException {
        if(event.getName().equals("help")) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setAuthor("Created By BOOM", "https://saweria.co/fkboom", event.getJDA().getSelfUser().getAvatarUrl());
            infoEmbed(embed);
            event.replyEmbeds(embed.build()).setEphemeral(true).queue();
        }

        if(event.getName().equals("ping")) {
            long time = System.currentTimeMillis();
            event.reply("Pong! ").setEphemeral(true).flatMap(v -> event.getHook().editOriginalFormat("Pong! %d ms", System.currentTimeMillis() - time)).queue();
        }

        if(event.getName().equals("info")) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.yellow);
            embed.setAuthor("Created By BOOM", "https://saweria.co/fkboom", event.getJDA().getSelfUser().getAvatarUrl());
            embed.setTitle(event.getJDA().getSelfUser().getName());
            embed.setDescription("Bot yang dibuat pas gabut doang, butuh dana buat nerusin ni projek kyknya");
            embed.setThumbnail(event.getJDA().getSelfUser().getAvatarUrl());
            embed.setFooter("Requested By " + event.getMember().getUser().getName());
            embed.setTimestamp(Instant.now());
            event.replyEmbeds(embed.build()).addActionRow(Button.primary("help", "Commands"), Button.of(ButtonStyle.LINK, "https://discord.gg/GTWMPv2", "My Own Guild"), Button.of(ButtonStyle.LINK, "https://saweria.co/fkboom", "Support Me")).queue();
        }
        
        if(event.getName().equals("say")) {
            if(event.getOption("anonim").getAsBoolean() == false) {
                event.reply(event.getOption("konten").getAsString()).queue();
            } else {
                event.reply("Terkirim").setEphemeral(true).queue();
                event.getChannel().sendMessage(event.getOption("konten").getAsString()).queue();
            }
        }
        
        if(event.getName().equals("avatar")) {
            EmbedBuilder embed = new EmbedBuilder();
            String ava;
            if(event.getOption("user").getAsMember().getAvatarUrl() != null) {
                ava = event.getOption("user").getAsMember().getAvatarUrl();
                embed.setImage(ava+"?size=4096");
            } else {
                ava = event.getOption("user").getAsUser().getAvatarUrl();
                embed.setImage(ava+"?size=4096");
            }
            event.replyEmbeds(embed.build()).queue();
        }
        
        if(event.getName().equals("embed")) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle(event.getOption("title").getAsString());
            embed.setDescription(event.getOption("description").getAsString());
            embed.setColor(Color.green);
            event.reply("Embed Terbuat").setEphemeral(true).queue();
            event.getTextChannel().sendMessageEmbeds(embed.build()).queue();
        }

        if(event.getName().equals("pilih")) {
            if(event.getSubcommandName().equals("angka")) {
                int val = Integer.valueOf(event.getOption("range").getAsString());
                String rand = String.valueOf(new Random().nextInt(val));
                event.reply("Pilihanku : " +rand).queue();
            }
            
            if(event.getSubcommandName().equals("huruf")) {
                List<String> list = new ArrayList<String>();
                list.add(event.getOption("1").getAsString());
                list.add(event.getOption("2").getAsString());
                if(event.getOption("3") != null) list.add(event.getOption("3").getAsString());
                if(event.getOption("4") != null) list.add(event.getOption("4").getAsString());
                if(event.getOption("5") != null) list.add(event.getOption("5").getAsString());
                if(event.getOption("6") != null) list.add(event.getOption("6").getAsString());
                if(event.getOption("7") != null) list.add(event.getOption("7").getAsString());
                if(event.getOption("8") != null) list.add(event.getOption("8").getAsString());
                if(event.getOption("9") != null) list.add(event.getOption("9").getAsString());
                String random = list.get(new Random().nextInt(list.size()));
                event.reply("Pilihanku : "+random).queue();
            }
        }
        
        if(event.getName().equals("voice")) {
            AudioManager audioManager = event.getGuild().getAudioManager();
            VoiceChannel member = event.getMember().getVoiceState().getChannel();
            VoiceChannel botChannel = event.getGuild().getSelfMember().getVoiceState().getChannel();
            if(event.getSubcommandName().equals("join")) {
                if(member == null) {
                    event.reply("Lu masuk dlu sono, ntar gw nyusul").setEphemeral(true).queue();
                } else if(!member.equals(botChannel) && botChannel != null) {
                    event.reply("Kita berada di dunia yg berbeda").setEphemeral(true).queue();
                } else {
                    audioManager.openAudioConnection(member);
                    event.reply("Gw udah masuk").setEphemeral(true).queue();
                }
            }
            
            if(event.getSubcommandName().equals("disconnect")) {
                if(member != null && member.equals(botChannel)) {
                    audioManager.closeAudioConnection();
                    event.reply("Rasis lu").setEphemeral(true).queue();
                } else {
                    event.reply("Kita berada di dunia yg berbeda").setEphemeral(true).queue();
                }
            }
        }
        
        if(event.getName().equals("music")) {
            AudioManager audioManager = event.getGuild().getAudioManager();
            VoiceChannel voiceChannel = event.getMember().getVoiceState().getChannel();
            VoiceChannel botChannel = event.getGuild().getSelfMember().getVoiceState().getChannel();

            if(event.getSubcommandName().equals("play")) {
                String link = event.getOption("judul").getAsString();
                if(!isUrl(link)) link = "ytsearch:" + link;

                if(voiceChannel == null) {
                    event.reply("Lu masuk dlu sono, ntar gw nyusul").setEphemeral(true).queue();
                } else if(!voiceChannel.equals(botChannel) && botChannel != null) {
                    event.reply("Kita berada di dunia yg berbeda").setEphemeral(true).queue();
                } else {
                    audioManager.openAudioConnection(voiceChannel);
                    PlayerManager.getInstance().loadAndPlay(event.getTextChannel(), link, event);
                }
            }
            
            if(event.getSubcommandName().equals("daftar")) {
                GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
                AudioPlayer audioPlayer = musicManager.player;
                AudioTrack track = audioPlayer.getPlayingTrack();

                if(voiceChannel == null) {
                    event.reply("Lu masuk dlu sono, ntar gw nyusul").setEphemeral(true).queue();
                    return;
                } else if(!voiceChannel.equals(botChannel) && botChannel != null) {
                    event.reply("Kita berada di dunia yg berbeda").setEphemeral(true).queue();
                    return;
                }
                if(track == null) {
                    event.reply("Playlist kosong").setEphemeral(true).queue();
                    return;
                }

                long milliseconds = track.getInfo().length;
                long minutes = (milliseconds / 1000) / 60;
                long seconds = (milliseconds / 1000) % 60;

                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("\uD83C\uDFB6 Sedang Diputar :");
                embed.setDescription("`" + track.getInfo().title + "`\n\n \uD83C\uDFA4 `" + track.getInfo().author + " (" + minutes + ":" + seconds +")`");
                embed.setThumbnail("https://i.ytimg.com/vi/" + track.getInfo().uri.substring(track.getInfo().uri.lastIndexOf("=") + 1) + "/sddefault.jpg");
                embed.setFooter("Requested By " + event.getMember().getUser().getName());
                embed.setTimestamp(Instant.now());
                event.replyEmbeds(embed.build()).addActionRow(Button.primary("semua", "List Playlist")).queue();
            }
            
            if(event.getSubcommandName().equals("skip")) {
                GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
                if(voiceChannel == null) {
                    event.reply("Lu masuk dlu sono, ntar gw nyusul").setEphemeral(true).queue();
                } else if(!voiceChannel.equals(botChannel) && botChannel != null) {
                    event.reply("Kita berada di dunia yg berbeda").setEphemeral(true).queue();
                } else {
                    musicManager.scheduler.nextTrack();
                    event.reply("Gasuka, yodah skip !").setEphemeral(true).queue();
                }
            }
            
            if(event.getSubcommandName().equals("stop")) {
                if(voiceChannel == null) {
                    event.reply("Lu masuk dlu sono, ntar gw nyusul").setEphemeral(true).queue();
                } else if(!voiceChannel.equals(botChannel) && botChannel != null) {
                    event.reply("Kita berada di dunia yg berbeda").setEphemeral(true).queue();
                } else {
                    GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
                    event.reply("Gasuka, yodah skip !").setEphemeral(true).queue();
                    musicManager.scheduler.player.stopTrack();
                    musicManager.scheduler.queue.clear();
                }
            }
        }
    }

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        if(event.getComponentId().equals("help")) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setAuthor("Created By BOOM", "https://saweria.co/fkboom", event.getJDA().getSelfUser().getAvatarUrl());
            infoEmbed(embed);
            event.replyEmbeds(embed.build()).setEphemeral(true).queue();
        }
        
        if(event.getComponentId().equals("semua")) {
            GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
            BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;
            AudioPlayer audioPlayer = musicManager.player;
            AudioTrack track = audioPlayer.getPlayingTrack();

            if(!queue.isEmpty()) {
                int trackCount = Math.min(queue.size(), 10);
                List<AudioTrack> trackList = new ArrayList<>(queue);
                EmbedBuilder embedQueue = new EmbedBuilder();
                embedQueue.setTitle("Sedang diputar : \n`" + track.getInfo().title + "`");
                embedQueue.setDescription("\uD83C\uDFA7 Lagu Selanjutnya :");

                for(int i = 0; i < trackCount; i++) {
                    AudioTrack trackQueue = trackList.get(i);
                    long millisecondsQueue = trackQueue.getInfo().length;
                    long minutesQueue = (millisecondsQueue / 1000) / 60;
                    long secondsQueue = (millisecondsQueue / 1000) % 60;
                    embedQueue.addField((i + 1)+". "+trackQueue.getInfo().title,"Oleh `"+trackQueue.getInfo().author+" ("+ minutesQueue + ":" + secondsQueue +")`" , false);
                }
                if(trackList.size() > 10) embedQueue.setFooter("Dan "+(trackList.size() - 10)+" lagu lagi...");
                event.replyEmbeds(embedQueue.build()).setEphemeral(true).queue();
            } else {
                event.reply("Playlist Kosong !!").setEphemeral(true).queue();
            }
        }
    }

    public void infoEmbed(EmbedBuilder embed) {
        embed.setColor(Color.green);
        embed.setTitle("Slash Commands (/)");
        embed.addField("\uD83D\uDCDD info", "Untuk melihat KTP ku", false);
        embed.addField("\uD83C\uDFD3 ping", "Test respon otakmu", false);
        embed.addField("\uD83D\uDDE3 say", "Lu nyuruh gw ngetik apa", false);
        embed.addField("\uD83D\uDDBC avatar", "Untuk melihat foto avatar user", false);
        embed.addField("\uD83C\uDF00 pilih", "`angka` Acak dalam bentuk angka \n `huruf` Acak dalam bentuk huruf", false);
        embed.addField("\uD83C\uDFB5 music", "`play` Lu mau dengerin gw nyanyi \n `daftar` Daftar musik yang diputar \n `skip` Ganti lagu \n `stop` Bebaskan aku dari tugas ini", false);
        embed.addField("\uD83D\uDCE2 voice", "`join` Butuh temen ya..., gw masuk situ dah \n `disconnect` Kick dari voice channel, rasis lu", false);
    }

    private boolean isUrl(String url) {
        try {
            new URI(url);
            return true;
        } catch(URISyntaxException e) {
            return false;
        }
    }
}

