package HotelBookingSystem;

public enum RoomType {
	
    SINGLE    ("Single Room",    "One single bed, ideal for solo travellers",   1, 100), 

    DOUBLE    ("Double Room",    "One double bed, ideal for couples",           2, 180), 

    TWIN      ("Twin Room",      "Two single beds, ideal for friends/siblings", 2, 150), 

    SUITE     ("Suite",          "Separate living area with king bed",          2, 200), 

    DELUXE    ("Deluxe Room",    "Premium furnishings with city view",          2, 300), 

    PENTHOUSE ("Penthouse Suite","Top floor, panoramic views, luxury fittings", 4, 500); 

  

    private final String displayName; 

    private final String description; 

    private final int defaultCapacity; 
    
    private final int price;

  

    private RoomType(String displayName, String description, int defaultCapacity, int Price) { 

        this.displayName = displayName; 

        this.description = description; 

        this.defaultCapacity = defaultCapacity; 
        
        this.price = Price;

    } 
    
    public static RoomType fromCode(char code) {
        return switch (Character.toUpperCase(code)) {
            case 'S' -> SINGLE;
            case 'D' -> DOUBLE;
            case 'T' -> TWIN;
            case 'U' -> SUITE;
            case 'L' -> DELUXE;
            case 'P' -> PENTHOUSE;
            default -> throw new IllegalArgumentException("Unknown room type code: " + code);
        };
    }


  

    public String getDisplayName()    { return displayName; } 

    public String getDescription()    { return description; } 

    public int getDefaultCapacity()   { return defaultCapacity; } 
    
    public int getPrice()             { return price; }


}
