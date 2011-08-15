package org.isolution.nexus.gateway;

import org.apache.commons.lang.NotImplementedException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * User: agwibowo
 * Date: 15/08/11
 * Time: 11:28 PM
 */
@ContextConfiguration(locations = {"classpath:spring-persistence.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class NexusGatewayEndpointAcceptanceTest {

    @Test
    public void shouldRouteMessageToTheCorrectEndpoint() {
        throw new NotImplementedException("Yet to be implemented");
    }

}
