package com.teamresourceful.resourcefulsettings.tasks;

import com.teamresourceful.ResourcefulSettingsPlugin;
import com.teamresourceful.resourcefulsettings.versioning.ModVersion;
import com.teamresourceful.resourcefulsettings.versioning.VersionProperties;
import org.gradle.api.DefaultTask;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;

import java.io.File;
import java.util.Map;

public class UpdateModVersionTask extends DefaultTask {

    private String patchVersion;
    private String versionPostfix;
    private String postfixBuild;

    @Option(option = "patchVersion", description = "Patch version for the mod")
    public void setPatchVersion(String patchVersion) {
        this.patchVersion = patchVersion;
    }

    @Input
    public String getPatchVersion() {
        return patchVersion;
    }

    @Input
    public String getVersionPostfix() {
        return versionPostfix;
    }

    @Option(option = "versionPostfix", description = "Postfix version for the mod (alpha/beta)")
    public void setVersionPostfix(String versionPostfix) {
        this.versionPostfix = versionPostfix;
    }

    @Input
    public String getPostfixBuild() {
        return postfixBuild;
    }

    @Option(option = "postfixBuild", description = "Postfix build version for the mod")
    public void setPostfixBuild(String postfixBuild) {
        this.postfixBuild = postfixBuild;
    }

    @OutputFile
    Provider<File> getOutputFile() {
        //I have no clue if there's a better way to do this or if it even works atm
        return getProject().getRootProject().provider(() -> getProject().getRootProject().getLayout().getProjectDirectory().file("version.properties").getAsFile());
    }

    @TaskAction
    public void updateVersion() {
        VersionProperties.update(Map.of("patch", patchVersion, "postfix", versionPostfix, "postfixBuild", postfixBuild));
        ModVersion newVersion = ModVersion.inferredVersion();
        VersionProperties.update("version", newVersion.toString());
        VersionProperties.writeToFile(getOutputFile().get().toPath());
        ResourcefulSettingsPlugin.LOGGER.quiet("Mod version has been updated: {}", newVersion);
    }


    /*@Input
    public abstract Property<String> getInitialMCVersion();

    @Input
    public abstract Property<String> getCurrentMCVersion();

    @Input
    public abstract Property<String> getPatchVersion();

    @Input
    @Optional
    public abstract Property<String> getVersionPostfix();

    @Input
    @Optional
    public abstract Property<String> getPostfixBuild();

    @Input
    @Optional
    public abstract Property<String> getBuildTime();

    @Input
    @Optional
    public abstract Property<Boolean> getIsNightly();

    @OutputFile
    Provider<File> getOutputFile() {
        //I have no clue if there's a better way to do this or if it even works atm
        return getProject().getRootProject().provider(() -> getProject().getRootProject().getLayout().getProjectDirectory().file("version.properties").getAsFile());
    }

    @TaskAction
    void run() throws IOException {
        if (getInitialMCVersion().get().equals("0.0.0") || getCurrentMCVersion().get().equals("0.0.0")) {
            throw new IllegalArgumentException("Initial and Current MC versions must be specified!");
            //may be a better error to throw but this will do for now
        }

        //set initial and current mc versions
        List<Integer> init = tokenizeVersionString(getInitialMCVersion().get());
        List<Integer> cur = tokenizeVersionString(getCurrentMCVersion().get());

        //determine mod major and minor
        int modMajor = cur.get(1) - init.get(1) + 1;
        int modMinor = Objects.equals(cur.get(1), init.get(1)) ? cur.get(2) - init.get(2) : cur.get(2);

        //create mod version string and apply postfix if available
        StringBuilder modVersionBuilder = new StringBuilder();
        modVersionBuilder.append(modMajor)
                .append(".")
                .append(modMinor)
                .append(".")
                .append(getPatchVersion().get());

        //add postfix if one exists
        if (!getVersionPostfix().get().isEmpty()) {
            modVersionBuilder.append("-").append(getVersionPostfix().get());
            if (!getPostfixBuild().get().isEmpty()) {
                modVersionBuilder.append(".").append(getPostfixBuild().get());
            }
        }

        //add build time if nightly version
        if (Boolean.TRUE.equals(getIsNightly().get())) {
            modVersionBuilder.append("+");
            if (!getBuildTime().get().isEmpty()) {
                modVersionBuilder.append(getBuildTime().get());
            } else {
                modVersionBuilder.append(System.currentTimeMillis() / 1000);
            }
        }

        String modVersion = modVersionBuilder.toString();
        LOGGER.quiet("Mod version has been updated: {}", modVersion);
        Files.write(getOutputFile().get().toPath(), ("version=" + modVersion).getBytes());
    }

    private static List<Integer> tokenizeVersionString(String string) {
        List<String> version = Arrays.asList(string.split("\\."));
        List<Integer> ret = new ArrayList<>(version.stream().mapToInt(Integer::parseInt).boxed().toList());
        if (ret.size() == 2) ret.add(0);
        return ret;
    }*/
}