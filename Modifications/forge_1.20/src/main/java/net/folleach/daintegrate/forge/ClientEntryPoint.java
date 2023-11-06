package net.folleach.daintegrate.forge;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(ClientEntryPoint.MODID)
public class ClientEntryPoint {
    public static final String MODID = "gaintegrate";
    private static final Logger LOGGER = LogUtils.getLogger();

    public ClientEntryPoint() {
        LOGGER.info("it's would works?");
    }
}
