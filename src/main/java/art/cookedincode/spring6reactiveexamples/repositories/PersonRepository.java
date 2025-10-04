package art.cookedincode.spring6reactiveexamples.repositories;

import art.cookedincode.spring6reactiveexamples.domain.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by Georgi Ivanov
 */
public interface PersonRepository {

    Mono<Person> getById(Integer id);

    Flux<Person> findAll();
}
