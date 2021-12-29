package valoeghese.denoiser;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class Denoiser implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LogManager.getLogger("denoiser");
	public static final Random RANDOM = new Random();

	@Override
	public void onInitialize() {
		LOGGER.info(funnyMessage[RANDOM.nextInt(funnyMessage.length - 1)]);
	}

	private static String[] funnyMessage = {
			"Why are you using this mod",
			"Why",
			"Valoeghese - doing the worldgen no one asked for since 2018",
			"\"Mod compatibility has gone from difficult to lmao\"",
			"Origin Valley 2 Electic Boogaloo",
			"Calculating the matrix vertices using the chonkamajig beardifier",
			"Writing funny startup messages instead of making the mod!",
			"No one asked for this",
			"Actually BOP kinda did but they decided it's not worth the effort",
			"If a datapacker saw this their eyes would explode",
			"If a datapacker saw this their ears would probably explode too",
			"Lambda, builder, lambda, FACTORY! Lambda, builder, lambda, FACTORY!",
			"Mixin :)",
			"Mixin :(",
			"Gosh damned datapackifying refactors >:(",
			"Stop reading the source code and experience these messages yourself you coward! ;)"
	};
}
