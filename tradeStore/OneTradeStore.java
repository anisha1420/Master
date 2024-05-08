package TradeStore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class OneTradeStore {
    private List<Trade> trades;

    public OneTradeStore() {
        this.trades = new ArrayList<>();
    }
    
    public void addTrade(Trade trade) {
    	try {
        // Checking if trade with lower version is being added
        for (Trade existingTrade : trades) {
            if (existingTrade.getTradeId().equals(trade.getTradeId()) && existingTrade.getVersion() > trade.getVersion()) {
                throw new Exception("Lower version trade is being added.");
            }
        }

        // Checking if trade's maturity date is not in the past
        if (!isValidMaturityDate(trade.getMaturityDate())) {
            throw new Exception("Trade maturity date cannot be in the past.");
        }

        // Checking if trade exists with the same version, if yes overriding it
        trades.removeIf(t -> t.getTradeId().equals(trade.getTradeId()) && t.getVersion() == trade.getVersion());
        
        trades.add(trade);
    	}
    	catch(Exception e) {
    		System.out.println("Exception occurred. "+e);
    	}
    }

    public void updateExpiredFlag() throws ParseException {
    	//trade flag is updated if in the existing list any data passes maturity date
        for (Trade trade : trades) {
            if (!isValidMaturityDate(trade.getMaturityDate())) {
                trade.setExpired("Y");
            }
        }
    }

    public List<Trade> getAllTrades() {
        return trades;
    }

    private boolean isValidMaturityDate(String maturityDate) throws ParseException {
        //checking maturity date is not in the past
        //it returns true if maturity date is equal or greater than today's date
    	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    	Date date = formatter.parse(maturityDate);
    	String str = formatter.format(new Date());
    	Date currentDate = formatter.parse(str);
    	
    	return date.compareTo(currentDate)>=0 ? true:false;
    }
    
}