package io.github.mooy1.infinitylib.core;

import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.plugin.PluginManagerMock;
import io.github.mooy1.otheraddon.MockOtherAddon;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TestAddonLive {

    private static PluginManagerMock manager;

    @BeforeAll
    public static void load() {
        manager = MockBukkit.mock().getPluginManager();
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @BeforeEach
    void clear() {
        manager.clearPlugins();
    }

    @Test
    void testNullInstance() {
        assertThrows(NullPointerException.class, AbstractAddon::instance);
    }

    @Test
    void testNoCommand() {
        assertDoesNotThrow(() -> MockBukkit.loadWith(MockAddon.class,
                new PluginDescriptionFile("MockAddon", "", MockAddon.class.getName())));
        assertThrows(NullPointerException.class, () -> MockAddon.instance().getAddonCommand());
    }

    @Test
    void testSharedInfinityLib() {
        PluginDescriptionFile desc = new PluginDescriptionFile("MockAddon", "", MockOtherAddon.class.getName());
        assertThrows(RuntimeException.class, () -> MockBukkit.load(MockOtherAddon.class, desc, Environment.LIBRARY_TESTING));
    }

    @Test
    void testBadGithubStrings() {
        assertThrows(RuntimeException.class,
                () -> MockBukkit.load(MockAddon.class, Environment.LIBRARY_TESTING, MockAddonTest.BAD_GITHUB_PATH));
    }

    @Test
    void testMissingAutoUpdateKey() {
        assertDoesNotThrow(
                () -> MockBukkit.load(MockAddon.class, Environment.LIBRARY_TESTING, MockAddonTest.MISSING_KEY));
    }

    @Test
    void testSuperEnable() {
        assertDoesNotThrow(
                () -> MockBukkit.load(MockAddon.class, Environment.LIBRARY_TESTING, MockAddonTest.CALL_SUPER));
        assertDoesNotThrow(
                () -> manager.disablePlugin(MockAddon.instance()));
    }

    @Test
    void testErrorThrown() {
        assertDoesNotThrow(
                () -> MockBukkit.load(MockAddon.class, Environment.LIBRARY_TESTING, MockAddonTest.THROW_EXCEPTION));
        assertDoesNotThrow(
                () -> manager.disablePlugin(MockAddon.instance()));
    }

}
