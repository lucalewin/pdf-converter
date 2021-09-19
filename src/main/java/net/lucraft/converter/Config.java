package net.lucraft.converter;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class Config {

	private static final Logger logger = LoggerFactory.getLogger(Config.class);

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
		logger.debug("loading config...");
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
			logger.debug("loaded default config");
			return;
		}

		try {
			String lines = Files.lines(CONFIG_FILE).collect(Collectors.joining(System.lineSeparator()));
			instance = GSON.fromJson(lines, Config.class);
			logger.debug("loaded config");
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	private static void createDefaultConfig() {
		logger.debug("loading default config");
		try {
			DEFAULT.save();
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
		logger.debug("saving config...");
		if (Files.notExists(ROOT_FOLDER)) {
			logger.debug("folder '" + ROOT_FOLDER.toFile().getAbsolutePath() + "' does not exist -> creating folder");
			Files.createDirectories(ROOT_FOLDER);
			logger.debug("created folder '" + ROOT_FOLDER.toFile().getAbsolutePath() + "'");
		}

		if (Files.notExists(CONFIG_FILE)) {
			logger.debug("config file does not exist -> creating new file...");
			Files.createFile(CONFIG_FILE);
			logger.debug("created config file");
		}

		String json = GSON.toJson(this);
		Files.writeString(CONFIG_FILE, json);
		logger.debug("saved config");
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
