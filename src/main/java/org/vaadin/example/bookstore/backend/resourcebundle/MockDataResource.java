package org.vaadin.example.bookstore.backend.resourcebundle;

import java.util.ListResourceBundle;

public class MockDataResource extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return new Object[][] {
                {"categoryNames", new String[] {
                        "Children's books", "Best sellers", "Romance", "Mystery",
                        "Thriller", "Sci-fi", "Non-fiction", "Cookbooks" }},
                {"word1", new String[] { "The art of", "Mastering",
                        "The secrets of", "Avoiding", "For fun and profit: ",
                        "How to fail at", "10 important facts about",
                        "The ultimate guide to", "Book of", "Surviving", "Encyclopedia of",
                        "Very much", "Learning the basics of", "The cheap way to",
                        "Being awesome at", "The life changer:", "The Vaadin way:",
                        "Becoming one with", "Beginners guide to",
                        "The complete visual guide to", "The mother of all references:" }},
                {"word2", new String[] { "gardening",
                        "living a healthy life", "designing tree houses", "home security",
                        "intergalaxy travel", "meditation", "ice hockey",
                        "children's education", "computer programming", "Vaadin TreeTable",
                        "winter bathing", "playing the cello", "dummies", "rubber bands",
                        "feeling down", "debugging", "running barefoot",
                        "speaking to a big audience", "creating software", "giant needles",
                        "elephants", "keeping your wife happy" }}
        };
    }
}

