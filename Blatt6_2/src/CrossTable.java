enum ShopGran {
    shop {
        @Override
        public ShopGran previous() {
            return this;
        }
    },
    city,
    region,
    country {
        @Override
        public ShopGran next() {
            return this;
        }
    };

    public ShopGran next() {
        return values()[ordinal() + 1];
    }
    public ShopGran previous() {
        return values()[ordinal() - 1];
    }
}

enum ProductGran {
    article {
        @Override
        public ProductGran previous() {
            return this;
        }
    },
    productGroup,
    productFamily,
    productCategory {
        @Override
        public ProductGran next() {
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
            return this;
        }
    },
    day,
    month,
    quarter,
    year {
        @Override
        public TimeGran next() {
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

    private ShopGran shopGran = ShopGran.shop;
    private ProductGran productGran = ProductGran.article;
    private TimeGran timeGran = TimeGran.day;


    public CrossTable() {
        System.out.println(shopGran.previous());
    }

    public void drillUp(Grans granToDrill) {
        switch (granToDrill) {
            case product:
                productGran.next();
                break;
            case Shop:
                shopGran.next();
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
                shopGran.previous();
                break;
            case time:
                timeGran.previous();
                break;
        }
    }

}
