package HotelBookingSystem;

public enum RoomType {

    SINGLE("Single Room", "One single bed, ideal for solo travellers", 1),
    DOUBLE("Double Room", "One double bed, ideal for couples", 2),
    TWIN("Twin Room", "Two single beds, ideal for friends/siblings", 2),
    SUITE("Suite", "Separate living area with king bed", 2),
    DELUXE("Deluxe Room", "Premium furnishings with city view", 2),
    PENTHOUSE("Penthouse Suite", "Top floor, panoramic views, luxury fittings", 4);

    private final String displayName;
    private final String description;
    private final int defaultCapacity;

    private RoomType(String displayName, String description, int defaultCapacity) {
        this.displayName = displayName;
        this.description = description;
        this.defaultCapacity = defaultCapacity;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public int getDefaultCapacity() {
        return defaultCapacity;
    }

}
