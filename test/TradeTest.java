package Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.jupiter.api.*;

import TradeStore.OneTradeStore;
import TradeStore.Trade;

class TradeTest {
	
	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
	String str = formatter.format(new Date());
	 //Data set provided which needs to be added
//  Trade trade1 = new Trade("T1", 1, "CP-1", "B1", "20/05/2020", str , "N");
//  Trade trade2 = new Trade("T2", 2, "CP-2", "B1", "20/05/2021", str , "N");
//	Trade trade3 = new Trade("T2", 1, "CP-1", "B1", "03/05/2021", "14/03/2015", "N");
//	Trade trade4 = new Trade("T3", 3, "CP-3", "B2", "20/05/2014", str , "Y");

    @Test
    void testAddTrade() {
        OneTradeStore tradeStore = new OneTradeStore();
        
        Trade trade1 = new Trade("T1", 1, "CP-1", "B1", str , "14/03/2019" , "N");
        // Adding a new trade
        tradeStore.addTrade(trade1);
        
        assertEquals(1, tradeStore.getAllTrades().size());
        assertEquals(trade1, tradeStore.getAllTrades().get(0));
    }

    @SuppressWarnings("unchecked")
	@Test
    void testAddTradeRejectLowerVersion() {
       try{
    	   OneTradeStore tradeStore = new OneTradeStore();
	        Trade trade1 = new Trade("T2", 1, "CP-1", "B1", str , str , "N");
	        Trade trade2 = new Trade("T2", 2, "CP-2", "B1", str , str , "N");
	
	        // Adding trade with higher version first
	        tradeStore.addTrade(trade2);
	        
	        // Adding trade with lower version, it should throw an exception
	        assertThrows(Exception("Lower version trade is being added."), () -> tradeStore.addTrade(trade1));
       } catch(Exception e) {
   		System.out.println("Exception occurred. "+e);
   		}
    }

    private Class Exception(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	@Test
    void testAddTradeOverrideSameVersion() {
        OneTradeStore tradeStore = new OneTradeStore();
        Trade trade1 = new Trade("T1", 1, "CP-1", "B1", str, str , "N");
        Trade trade1SameVersion = new Trade("T1", 1, "CP-1", "B1", str, str , "N");

        // Adding trade with version 1
        tradeStore.addTrade(trade1);
        
        // Adding trade with same version, it should override the existing trade
        tradeStore.addTrade(trade1SameVersion);
        
        assertEquals(1, tradeStore.getAllTrades().size());
        assertEquals(trade1SameVersion, tradeStore.getAllTrades().get(0));
    }

    @Test
    void testAddTradeRejectPastMaturityDate() {
    	try {
	        OneTradeStore tradeStore = new OneTradeStore();
	        Trade trade1 = new Trade("T1", 1, "CP-1", "B1", "20/05/2020", "03/05/2019", "N");
	
	        // Adding trade with maturity date in the past, it should throw an exception
	        assertThrows(Exception("Trade maturity date cannot be in the past."), () -> tradeStore.addTrade(trade1));
    	} catch(Exception e) {
       		System.out.println("Exception occurred. "+e);
       		}
    }

    @Test
    void testUpdateExpiredFlag() throws ParseException {
        OneTradeStore tradeStore = new OneTradeStore();
        Trade trade1 = new Trade("T1", 1, "CP-1", "B1", "20/05/2024", "03/05/2022", "N");

        // Adding trade with maturity date in the future
        tradeStore.addTrade(trade1);

        // After maturity date has passed, expired flag should be updated to 'Y'
        // Please note: to test with a date passed maturity date, 
        // date needs to be lower than todays date which gives "Trade maturity date cannot be in the past" exception and the list doesn't get added.
        tradeStore.updateExpiredFlag();
        assertEquals("Y", tradeStore.getAllTrades().get(0).getExpired());
    }
}