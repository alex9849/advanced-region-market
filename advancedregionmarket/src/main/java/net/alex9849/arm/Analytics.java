package net.alex9849.arm;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

class Analytics {
    private final File confFile;
    private final YamlConfiguration config;
    private Plugin plugin;

    Analytics(Plugin plugin) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null!");
        }
        this.plugin = plugin;
        this.confFile = new File(plugin.getDataFolder() + "/analytics.yml");
        this.config = YamlConfiguration.loadConfiguration(this.confFile);
        this.config.options().copyDefaults(true).copyHeader(true);
        if(this.config.get("enabled") == null) {
            this.config.addDefault("enabled", true);
            this.config.options().header("This plugin collects some data like the online players, \n" +
                    "ip address, port, etc. so I can visit your server. To see what you do with my plugin helps \n" +
                    "to motivate myself to continue to develop this plugin. If you don''t want that \n" +
                    "just set \"enabled\" to false. All collected data will be deleted automatically, \n" +
                    "after 2 weeks. Alternatively you can delete your data manually by pasting your \n" +
                    "installId under this link: https://mcplug.alex9849.net/unregister\n" +
                    "If you don''t see an installId in this file, no data has been sent. Thanks for using this plugin!");
            try {
                this.config.save(confFile);
            } catch (IOException e) {
                //Ignore
            }
        }
        if(this.config.getBoolean("enabled")) {
            startSubmitting();
        }
    }

    private void startSubmitting() {
        final Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!plugin.isEnabled()) {
                    timer.cancel();
                    return;
                }
                Bukkit.getScheduler().runTask(plugin, () -> {
                    Thread sendThread = new Thread(() -> {
                        try {
                            submitData();
                        } catch (Exception e) {
                            //Ignore
                            e.printStackTrace();
                        }
                    });
                    sendThread.start();
                });
            }
        }, 1000 * 10, 1000 * 60 * 5);
    }

    private JSONObject getData() {
        JSONObject data = new JSONObject();

        String installId = this.config.getString("installId");
        data.put("serverVersion", Bukkit.getVersion());
        data.put("serverPort", Bukkit.getPort());
        data.put("modt", Bukkit.getServer().getMotd());
        data.put("serverIp", Bukkit.getIp());
        data.put("onlinePlayers", Bukkit.getOnlinePlayers().size());
        data.put("onlineMode", Bukkit.getOnlineMode());
        data.put("pluginName", this.plugin.getName());
        data.put("pluginVersion", this.plugin.getDescription().getVersion());
        if(installId != null) {
            data.put("installId", installId);
        }
        return data;
    }

    private synchronized void submitData() throws IOException, ParseException, ExecutionException, InterruptedException {
        final String databaseUrl = "http://localhost:8080/web_war/sendstats";
        Future<JSONObject> sendDataFuture = Bukkit.getScheduler().callSyncMethod(plugin, this::getData);
        JSONObject sendData = sendDataFuture.get();

        HttpURLConnection connection = (HttpURLConnection) new URL(databaseUrl).openConnection();
        connection.setRequestProperty("User-Agent", "Alex9849 Plugin");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        PrintStream ps = new PrintStream(connection.getOutputStream());
        ps.print(sendData.toJSONString());
        ps.flush();
        ps.close();

        InputStream inputStream = connection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }
        bufferedReader.close();

        if(connection.getResponseCode() != 200) {
            return;
        }
        JSONParser jsonParser = new JSONParser();
        JSONObject response = (JSONObject) jsonParser.parse(sb.toString());
        String installId = (String) response.get("installId");

        if(!Objects.equals(this.config.getString("installId"), installId)) {
            this.config.set("installId", installId);

            Future<Boolean> done = Bukkit.getScheduler().callSyncMethod(plugin, () -> {
                this.config.save(this.confFile);
                return true;
            });
            done.get();

        }
    }
}
