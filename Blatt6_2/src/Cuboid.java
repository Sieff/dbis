public class Cuboid {

    private int value;
    private String branchLabel;
    private String timeLabel;
    private String productLabel;

    public Cuboid() { }

    public void print(int lineLength) {
        String localBranchLabel = getBranchLabel() == null ? "All" : getBranchLabel();
        String localProductLabel = getProductLabel() == null ? "All" : getProductLabel();
        String localTimeLabel = getTimeLabel() == null ? "All" : getTimeLabel();

        System.out.format(
                "| %" + lineLength * 2 + "s | %" + lineLength * 2 + "s | %" + lineLength + "s | %" + lineLength + "d |\n",
                localBranchLabel, localProductLabel, localTimeLabel, value
        );
    }


    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getBranchLabel() {
        return branchLabel;
    }

    public void setBranchLabel(String shopLabel) {
        this.branchLabel = shopLabel;
    }

    public String getProductLabel() {
        return productLabel;
    }

    public void setProductLabel(String productLabel) {
        this.productLabel = productLabel;
    }

    public String getTimeLabel() {
        return timeLabel;
    }

    public void setTimeLabel(String timeLabel) {
        this.timeLabel = timeLabel;
    }
}
