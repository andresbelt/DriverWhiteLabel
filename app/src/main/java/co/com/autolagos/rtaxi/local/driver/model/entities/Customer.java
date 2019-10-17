package co.com.autolagos.rtaxi.local.driver.model.entities;

public class Customer {

    private String uid;
    private String name;
    private String avatar;
    private String thumbnail;
    private float score;
    private Journey journey;
    private int state;

    public Customer() {}
    public Customer(String uid, String name, String avatar, String thumbnail, float score, Journey journey, int state) {
        this.uid = uid;
        this.name = name;
        this.avatar = avatar;
        this.thumbnail = thumbnail;
        this.score = score;
        this.journey = journey;
        this.state = state;
    }

    //Getters
    public String getUid() {
        return uid;
    }
    public String getName() {
        return name;
    }
    public String getAvatar() {
        return avatar;
    }
    public String getThumbnail() {
        return thumbnail;
    }
    public float getScore() {
        return score;
    }
    public Journey getJourney() {
        return journey;
    }
    public int getState() {
        return state;
    }

    //Setters
    public void setUid(String uid) {
        this.uid = uid;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
    public void setScore(float score) {
        this.score = score;
    }
    public void setJourney(Journey journey) {
        this.journey = journey;
    }
    public void setState(int state) {
        this.state = state;
    }

    //ToString
    @Override
    public String toString() {
        return "Customer{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", score=" + score +
                '}';
    }

}
