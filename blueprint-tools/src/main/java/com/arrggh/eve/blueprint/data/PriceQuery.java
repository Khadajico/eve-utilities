package com.arrggh.eve.blueprint.data;

import com.arrggh.eve.blueprint.model.EvePriceHistory;
import com.arrggh.eve.blueprint.model.MarketPrice;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import static org.apache.logging.log4j.LogManager.getLogger;

public class PriceQuery {
    private static final long DEFAULT_TIME = 0;
    private static final double DEFAULT_PRICE = 9999.99;

    private static final Logger LOG = getLogger(PriceQuery.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    private MarketPriceCache cache;

    public PriceQuery(MarketPriceCache cache) {
        this.cache = cache;
    }

    private static final int marketId = 10000002;

    public double queryPrice(int typeId) {
        // Get it from the cache if possible
        MarketPrice cachedPrice = cache.getPrice(marketId, typeId);
        if (cachedPrice != null) {
            return cachedPrice.getPrice();
        }

        // Do a lookup otherwise
        MarketPrice entry = fetchPriceFromCrest(typeId, marketId);
        cache.addPrice(marketId, typeId, entry);
        return entry.getPrice();
    }


    private MarketPrice fetchPriceFromCrest(int typeId, int marketId) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String url = String.format("https://public-crest.eveonline.com/market/%d/types/%d/history/", marketId, typeId);
        HttpGet httpGet = new HttpGet(url);

        long time = DEFAULT_TIME;
        double price = DEFAULT_PRICE;

        LOG.info("querying url '" + httpGet.getURI() + "'");
        try (CloseableHttpResponse queryResponse = httpclient.execute(httpGet)) {
            if (queryResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity1 = queryResponse.getEntity();
                String content = EntityUtils.toString(entity1);
                EvePriceHistory priceHistory = mapper.readValue(content, EvePriceHistory.class);
                EntityUtils.consume(entity1);
                time = System.nanoTime();
                price = priceHistory.findLatestPrice();
                LOG.info("Query success '" + url + "'");
            } else {
                LOG.info("Query failed with status '" + queryResponse.getStatusLine() + "'");
            }
        } catch (IOException e) {
            LOG.error("Cannot get price for type " + typeId + " in market " + marketId + ": " + e.getMessage());
        }
        return MarketPrice.builder().time(time).typeId(typeId).marketId(marketId).price(price).build();
    }
}
