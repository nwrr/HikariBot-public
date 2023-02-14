package HikariBot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.json.JSONException;
import org.json.JSONObject;

public class Main {

    public static JDA api;
    public static String prefix = "~";
    
    public static void main(String[] args) throws IOException, LoginException, InterruptedException{
        String token = "src/main/java/HikariBot/config.json";
        JSONObject jsonObject = parseJSONFile(token);
        
        api = JDABuilder.createDefault(jsonObject.getString("token")).build().awaitReady();
        api.getPresence().setPresence(OnlineStatus.ONLINE, Activity.playing("Mengudara~"));
        api.addEventListener(new Commands());
        api.updateCommands()
                .addCommands(new CommandData("help", "Semua perintah slash command"))
                .addCommands(new CommandData("info", "Buat liat KTP ku"))
                .addCommands(new CommandData("ping", "Test respon otakmu"))
                .addCommands(new CommandData("say", "Lu nyuruh gw ngetik apa")
                        .addOption(OptionType.STRING, "konten", "Lu mau ngetik apa", true)
                        .addOption(OptionType.BOOLEAN, "anonim", "tampilkan diri anda", true))
                .addCommands(new CommandData("embed", "Ketikkan Pesan Embed")
                        .addOption(OptionType.STRING, "title", "Menambah Title pada Embed", true)
                        .addOption(OptionType.STRING, "description", "Mebambah Deskripsi pada Embed", true))
                .addCommands(new CommandData("avatar", "Tampilkan pp avatarmu")
                        .addOption(OptionType.USER, "user", "Pilih user yang mau ditampilkan", true))
                .addCommands(new CommandData("voice", "Menu Voice")
                        .addSubcommands(new SubcommandData("join", "Butuh temen ya..., gw masuk situ dah"))
                        .addSubcommands(new SubcommandData("disconnect", "Kick dari voice channel, rasis lu")))
                .addCommands(new CommandData("music", "Menu Musik")
                        .addSubcommands(new SubcommandData("play", "Lu mau dengerin gw nyanyi")
                                .addOption(OptionType.STRING, "judul", "Artis / Judul / Link nya paan", true))
                        .addSubcommands(new SubcommandData("daftar", "Daftar musik yang diputar"))
                        .addSubcommands(new SubcommandData("skip", "Ganti lagu"))
                        .addSubcommands(new SubcommandData("stop", "Bebaskan aku dari tugas ini")))
                .addCommands(new CommandData("pilih", "Bingung, sini biar gw pilihin")
                        .addSubcommands(new SubcommandData("angka", "Acak dalam bentuk angka")
                                .addOption(OptionType.NUMBER, "range", "Masukkan range yang akan diacak", true))
                        .addSubcommands(new SubcommandData("huruf", "Acak dalam bentuk huruf")
                                .addOption(OptionType.STRING, "1", "Pilihan Pertama", true)
                                .addOption(OptionType.STRING, "2", "Pilihan Kedua", true)
                                .addOption(OptionType.STRING, "3", "Pilihan Ketiga")
                                .addOption(OptionType.STRING, "4", "Pilihan Keempat")
                                .addOption(OptionType.STRING, "5", "Pilihan Kelima")
                                .addOption(OptionType.STRING, "6", "Pilihan Keenam")
                                .addOption(OptionType.STRING, "7", "Pilihan Ketujuh")
                                .addOption(OptionType.STRING, "8", "Pilihan Kedelapan")
                                .addOption(OptionType.STRING, "9", "Pilihan Kesembilan")))
                .queue();
    }
    
    public static JSONObject parseJSONFile(String token) throws JSONException, IOException {
        String content = new String(Files.readAllBytes(Paths.get(token)));
        return new JSONObject(content);
    }
}
