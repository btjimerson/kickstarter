package dev.snbv2.kickstarter.types;

import java.util.List;

public class Container {
    private String name;
    private String image;
    private Integer port;
    private ImagePullPolicy imagePullPolicy;
    private List<Integer> containerPorts;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ImagePullPolicy getImagePullPolicy() {
        return imagePullPolicy;
    }

    public void setImagePullPolicy(ImagePullPolicy imagePullPolicy) {
        this.imagePullPolicy = imagePullPolicy;
    }

    public List<Integer> getContainerPorts() {
        return containerPorts;
    }

    public void setContainerPorts(List<Integer> containerPorts) {
        this.containerPorts = containerPorts;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public enum ImagePullPolicy {
        ALWAYS("Always"),
        NEVER("Never"),
        IF_NOT_PRESENT("IfNotPresent");
    
        private String imagePullPolicy;
    
    
        private ImagePullPolicy(String imagePullPolicy) {
            this.imagePullPolicy = imagePullPolicy;
        }
        
        public String getValue() {
            return imagePullPolicy;
        }
    
        public static ImagePullPolicy findByValue(String value) {
            for (ImagePullPolicy p : values()) {
                if (p.getValue().equalsIgnoreCase(value)) {
                    return p;
                }
            }
            return null;
        }
    }

}
