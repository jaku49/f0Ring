package me.f0rant.f0ring.model;

public class PlayerData {
    private RingData activeRing;
    private boolean hideSelf;
    private boolean hideOthers;
    private boolean resumeRingo; 

    public PlayerData(RingData activeRing, boolean hideSelf, boolean hideOthers, boolean resumeRingo) {
        this.activeRing = activeRing;
        this.hideSelf = hideSelf;
        this.hideOthers = hideOthers;
        this.resumeRingo = resumeRingo;
    }

    public RingData getActiveRing() { return activeRing; }
    public void setActiveRing(RingData activeRing) { this.activeRing = activeRing; }

    public boolean isHideSelf() { return hideSelf; }
    public void setHideSelf(boolean hideSelf) { this.hideSelf = hideSelf; }

    public boolean isHideOthers() { return hideOthers; }
    public void setHideOthers(boolean hideOthers) { this.hideOthers = hideOthers; }

    public boolean isResumeRingo() { return resumeRingo; }
    public void setResumeRingo(boolean resumeRingo) { this.resumeRingo = resumeRingo; }
}