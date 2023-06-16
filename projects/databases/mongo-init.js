db.createUser(
    {
        user: "Giulio",
        pwd: "bachelor2023",
        roles: [
            {
                role: "readWrite",
                db: "Game"
            }
        ]
    }
);

db.createCollection("Characters");

db.createCollection("Items");

db.createCollection("Obstacles");