
import com.google.protobuf.ByteString;
import tensorflow.serving.FileSystemStoragePathSource.FileSystemStoragePathSourceConfig.ServableVersionPolicy;
import tensorflow.serving.ModelServerConfigOuterClass.ModelConfigList;
import tensorflow.serving.ModelServerConfigOuterClass.ModelServerConfig;
import tensorflow.serving.ModelServerConfigOuterClass.ModelConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class model_config_utils {

    public static ModelConfig.Builder buildModelConfig(String name, String basePath) {
        ModelConfig.Builder modelConfigBuilder = ModelConfig.newBuilder();
        modelConfigBuilder.setName(name);
        modelConfigBuilder.setBasePath(basePath);
        return modelConfigBuilder;
    }

    public static ModelConfig.Builder buildModelConfig(String name, String basePath, Map<String, Long> versionLabelsMap) {
        ModelConfig.Builder modelConfigBuilder = buildModelConfig(name, basePath);
        modelConfigBuilder.putAllVersionLabels(versionLabelsMap);
        return modelConfigBuilder;
    }

    public static ModelConfig.Builder buildLatestModelConfig(String name, String basePath, Integer numOfVersions) {
        ServableVersionPolicy.Builder servableVersionPolicyBuilder = ServableVersionPolicy.newBuilder();
        ServableVersionPolicy.Latest.Builder latestBuilder = ServableVersionPolicy.Latest.newBuilder();
        latestBuilder.setNumVersions(numOfVersions);
        servableVersionPolicyBuilder.setLatest(latestBuilder);

        ModelConfig.Builder modelConfigBuilder = buildModelConfig(name, basePath);
        modelConfigBuilder.setModelVersionPolicy(servableVersionPolicyBuilder);
        return modelConfigBuilder;
    }

    public static ModelConfig.Builder buildSpecificModelConfig(String name, String basePath,
                                                               List<Long> specificVersions) {
        ServableVersionPolicy.Builder servableVersionPolicyBuilder = ServableVersionPolicy.newBuilder();
        ServableVersionPolicy.Specific.Builder specificBuilder = ServableVersionPolicy.Specific.newBuilder();
        for (Long version : specificVersions) {
            specificBuilder.addVersions(version);
        }
        servableVersionPolicyBuilder.setSpecific(specificBuilder);

        ModelConfig.Builder modelConfigBuilder = buildModelConfig(name, basePath);
        modelConfigBuilder.setModelVersionPolicy(servableVersionPolicyBuilder);
        return modelConfigBuilder;
    }

    public static ModelConfig.Builder buildLatestModelConfig(String name, String basePath, Integer numOfVersions, Map<String, Long> versionLabelsMap) {
        ServableVersionPolicy.Builder servableVersionPolicyBuilder = ServableVersionPolicy.newBuilder();
        ServableVersionPolicy.Latest.Builder latestBuilder = ServableVersionPolicy.Latest.newBuilder();
        latestBuilder.setNumVersions(numOfVersions);
        servableVersionPolicyBuilder.setLatest(latestBuilder);

        ModelConfig.Builder modelConfigBuilder = buildModelConfig(name, basePath, versionLabelsMap);
        modelConfigBuilder.setModelVersionPolicy(servableVersionPolicyBuilder);
        return modelConfigBuilder;
    }

    public static ModelConfig.Builder buildSpecificModelConfig(String name, String basePath,
                                                               List<Long> specificVersions, Map<String, Long> versionLabelsMap) {
        ServableVersionPolicy.Builder servableVersionPolicyBuilder = ServableVersionPolicy.newBuilder();
        ServableVersionPolicy.Specific.Builder specificBuilder = ServableVersionPolicy.Specific.newBuilder();
        for (Long version : specificVersions) {
            specificBuilder.addVersions(version);
        }
        servableVersionPolicyBuilder.setSpecific(specificBuilder);

        ModelConfig.Builder modelConfigBuilder = buildModelConfig(name, basePath, versionLabelsMap);
        modelConfigBuilder.setModelVersionPolicy(servableVersionPolicyBuilder);
        return modelConfigBuilder;
    }

    public static void main(String[] args) throws Exception {
        ModelServerConfig.Builder serverConfigBuilder = ModelServerConfig.newBuilder();

        ModelConfig.Builder modelConfigBuilder1 = buildLatestModelConfig("aa", "/root", 2);

        List<Long> specificVersions = new ArrayList<>();
        specificVersions.add(1L);
        specificVersions.add(2L);

        Map<String, Long> versionLabelsMap = new HashMap<>();
        versionLabelsMap.put("stable", 1L);
        versionLabelsMap.put("canary", 2L);

        ModelConfig.Builder modelConfigBuilder2 = buildSpecificModelConfig("bb", "/root", specificVersions, versionLabelsMap);

        ModelConfigList.Builder configListBuilder = ModelConfigList.newBuilder();
        configListBuilder.addConfig(modelConfigBuilder1);
        configListBuilder.addConfig(modelConfigBuilder2);
        serverConfigBuilder.setModelConfigList(configListBuilder);
        ModelServerConfig modelServerConfig = serverConfigBuilder.build();

        String configString = modelServerConfig.toString();
        ByteString configByteString = modelServerConfig.toByteString();

        System.out.println(configByteString.toStringUtf8());


        ModelServerConfig serverConfig2 =
                ModelServerConfig.parseFrom(configByteString);
        System.out.println(serverConfig2.toString());

        System.out.println(serverConfig2.toString());
    }


}
