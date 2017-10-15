package com.mtg.app;

import io.magicthegathering.javasdk.api.CardAPI;
import io.magicthegathering.javasdk.resource.Card;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        int multverseId = 1;
        Card card = CardAPI.getCard(multverseId);

        System.out.println( card.getImageUrl() );

    }

}
