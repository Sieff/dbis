import java.util.ArrayList;
import java.util.List;

enum BranchGran {
    shop {
        @Override
        public BranchGran previous() {
            System.out.println("Already lowest granulate!");
            return this;
        }
    },
    city,
    region,
    country {
        @Override
        public BranchGran next() {
            System.out.println("Already highest granulate!");
            return this;
        }
    };

    public BranchGran next() {
        return values()[ordinal() + 1];
    }
    public BranchGran previous() {
        return values()[ordinal() - 1];
    }
}

enum ProductGran {
    article {
        @Override
        public ProductGran previous() {
            System.out.println("Already lowest granulate!");
            return this;
        }
    },
    product_group,
    product_family,
    product_category {
        @Override
        public ProductGran next() {
            System.out.println("Already highest granulate!");
            return this;
        }
    };

    public ProductGran next() {
        return values()[ordinal() + 1];
    }
    public ProductGran previous() {
        return values()[ordinal() - 1];
    }
}

enum TimeGran {
    date {
        @Override
        public TimeGran previous() {
            System.out.println("Already lowest granulate!");
            return this;
        }
    },
    day,
    month,
    quarter,
    year {
        @Override
        public TimeGran next() {
            System.out.println("Already highest granulate!");
            return this;
        }
    };

    public TimeGran next() {
        return values()[ordinal() + 1];
    }
    public TimeGran previous() {
        return values()[ordinal() - 1];
    }
}

enum Grans {
    product, time, Shop
}

public class CrossTable {

    private BranchGran branchGran = BranchGran.shop;
    private ProductGran productGran = ProductGran.article;
    private TimeGran timeGran = TimeGran.day;
    private List<Cuboid> cuboidList = new ArrayList<>();

    private boolean aggregateBranch = false;
    private boolean aggregateProduct = false;
    private boolean aggregateTime = false;

    private int lineLength = 15;
    private int numberOfRows;
    private int currentPage;


    public void drillUp(Grans granToDrill) {
        switch (granToDrill) {
            case product:
                productGran.next();
                break;
            case Shop:
                branchGran.next();
                break;
            case time:
                timeGran.next();
                break;
        }
    }

    public void drillDown(Grans granToDrill) {
        switch (granToDrill) {
            case product:
                productGran.previous();
                break;
            case Shop:
                branchGran.previous();
                break;
            case time:
                timeGran.previous();
                break;
        }
    }

    public void nextPage() {
        if (currentPage + 1 < (countValidCuboids() - 1) / numberOfRows) {
            this.currentPage++;
            printData();
        } else {
            System.out.println("Already on last page5!");
        }

    }

    private int countValidCuboids() {
        int i = 0;
        for (Cuboid cuboid: cuboidList) {
            if (isValid(cuboid)) {
                i++;
            }
        }
        return i;
    }

    public void previousPage() {
        if (currentPage == 0) {
            System.out.println("Already on first page!");
        } else {
            this.currentPage--;
            printData();
        }
    }

    public void printData() {
        printFrame();
        System.out.format("| %" + lineLength * 2 + "s | %" + lineLength * 2 + "s | %" + lineLength + "s | %" + lineLength + "s |\n", branchGran, productGran, timeGran, "Total");
        printFrame();
        print();
        printFrame();
    }

    private void printFrame() {
        System.out.print("| ");
        for (int i = 0; i < 6 * lineLength + (3 * 3); i++) {
            System.out.print("-");
        }
        System.out.println(" |");
    }

    private void print() {
        int numberOfValidEntries = 0;
        int entriesPrinted = 0;
        int currentEntryIndex = 0;
        while (entriesPrinted < numberOfRows) {
            if (currentEntryIndex == cuboidList.size()) {
                //System.out.format("%" + 6 * lineLength + (3 * 3) + "s\n", "Reached End of Data!");
                break;
            }
            Cuboid cuboid = cuboidList.get(currentEntryIndex);
            boolean valid = isValid(cuboid);
            if (valid) {
                if (numberOfValidEntries >= currentPage * numberOfRows && numberOfValidEntries < (currentPage + 1) * numberOfRows) {
                    cuboid.print(lineLength);
                    entriesPrinted++;
                }
                currentEntryIndex++;
                numberOfValidEntries++;
            } else {
                currentEntryIndex++;
            }
        }
    }

    private boolean isValid(Cuboid cuboid) {
        if (isAggregateBranch() != (cuboid.getBranchLabel() == null)) {
            return false;
        }
        if (isAggregateProduct() != (cuboid.getProductLabel() == null)) {
            return false;
        }
        if (isAggregateTime() != (cuboid.getTimeLabel() == null)) {
            return false;
        }
        return true;
    }

    public boolean isAggregateBranch() {
        return aggregateBranch;
    }

    public void setAggregateBranch(boolean aggregateBranch) {
        this.aggregateBranch = aggregateBranch;
    }

    public boolean isAggregateProduct() {
        return aggregateProduct;
    }

    public void setAggregateProduct(boolean aggregateProduct) {
        this.aggregateProduct = aggregateProduct;
    }

    public boolean isAggregateTime() {
        return aggregateTime;
    }

    public void setAggregateTime(boolean aggregateTime) {
        this.aggregateTime = aggregateTime;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void add(Cuboid cuboid) {
        cuboidList.add(cuboid);
    }

    public void clearData() {
        cuboidList.clear();
    }

    public void setLineLength(int lineLength) {
        this.lineLength = lineLength;
    }

    public void setBranchGran(BranchGran branchGran) {
        this.branchGran = branchGran;
    }

    public BranchGran getBranchGran() {
        return branchGran;
    }

    public void setProductGran(ProductGran productGran) {
        this.productGran = productGran;
    }

    public ProductGran getProductGran() {
        return productGran;
    }

    public void setTimeGran(TimeGran timeGran) {
        this.timeGran = timeGran;
    }

    public TimeGran getTimeGran() {
        return timeGran;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }
}
