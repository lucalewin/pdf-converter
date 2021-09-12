package net.lucraft.converter;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class Config {

	private static final Path ROOT_FOLDER;
	private static final Path CONFIG_FILE;
	private static final Config DEFAULT;

	private static final Gson GSON;

	static {
		ROOT_FOLDER = Path.of(System.getenv("LocalAppData"), "lucraft", "converter");
		CONFIG_FILE = ROOT_FOLDER.resolve("config.json");
		DEFAULT = new Config(
				FileUtils.getUserDirectoryPath(),
				"out.pdf",
				"");

		GSON = new Gson();
	}

	private static Config instance;

	public static Config getInstance() {
		if (instance == null) load();
		return instance;
	}

	public static void load() {
		if (Files.notExists(ROOT_FOLDER)) {
			try {
				Files.createDirectories(ROOT_FOLDER);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (Files.notExists(CONFIG_FILE)) {
			createDefaultConfig();
			instance = DEFAULT;
			return;
		}

		try {
			String lines = Files.lines(CONFIG_FILE).collect(Collectors.joining(System.lineSeparator()));
			instance = GSON.fromJson(lines, Config.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void createDefaultConfig() {
		try {
			if (Files.notExists(ROOT_FOLDER)) {
				Files.createDirectories(ROOT_FOLDER);
			}

			if (Files.notExists(CONFIG_FILE)) {
				Files.createFile(CONFIG_FILE);
			}

			String json = GSON.toJson(DEFAULT);
			Files.writeString(CONFIG_FILE, json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String defaultFolder;
	private String defaultFilename;
	private String lastFileLocation;

	public Config() {}

	protected Config(String defaultFolder, String defaultFilename, String lastFileLocation) {
		this.defaultFolder = defaultFolder;
		this.defaultFilename = defaultFilename;
		this.lastFileLocation = lastFileLocation;
	}

	public void save() throws IOException {
		if (Files.notExists(ROOT_FOLDER)) {
			Files.createDirectories(ROOT_FOLDER);
		}

		if (Files.notExists(CONFIG_FILE)) {
			Files.createFile(CONFIG_FILE);
		}

		String json = GSON.toJson(this);
		Files.writeString(CONFIG_FILE, json);
	}

	public String getDefaultFolder() {
		return defaultFolder;
	}

	public String getDefaultFilename() {
		return defaultFilename;
	}

	public String getLastFileLocation() {
		return lastFileLocation;
	}

	public void setDefaultFolder(String defaultFolder) {
		this.defaultFolder = defaultFolder;
	}

	public void setDefaultFilename(String defaultFilename) {
		this.defaultFilename = defaultFilename;
	}

	public void setLastFileLocation(String lastFileLocation) {
		this.lastFileLocation = lastFileLocation;
	}
}
