package art.cookedincode.spring6reactiveexamples.repositories;

import art.cookedincode.spring6reactiveexamples.domain.Person;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Georgi Ivanov
 */
class PersonRepositoryImplTest {

    PersonRepository personRepository = new PersonRepositoryImpl();

    @Test
    void testMonoByIdBlock() {
        Mono<Person> personMono = personRepository.getById(1);

        Person person = personMono.block();

        System.out.println(person);
    }

    @Test
    void testGetByIdSubscriber() {
        Mono<Person> personMono = personRepository.getById(1);

        personMono.subscribe(System.out::println);
    }

    @Test
    void testMapOperation() {
        Mono<Person> personMono = personRepository.getById(1);

        personMono
                .map(Person::getFirstName)
                .subscribe(System.out::println);
    }

    @Test
    void testFluxBlock() {
        Flux<Person> personFlux = personRepository.findAll();

        Person person = personFlux.blockFirst();

        System.out.println(person);
    }

    @Test
    void testFluxSubscriber() {
        Flux<Person> personFlux = personRepository.findAll();

        personFlux.subscribe(System.out::println);
    }

    @Test
    void testFluxMap() {
        Flux<Person> personFlux = personRepository.findAll();

        personFlux.map(Person::getFirstName).subscribe(System.out::println);
    }

    @Test
    void testFluxToList() {
        Flux<Person> personFlux = personRepository.findAll();

        Mono<List<Person>> listMono = personFlux.collectList();

        listMono.subscribe(list -> {
            list.forEach(person -> System.out.println(person.getFirstName()));
        });
    }

    @Test
    void testFilterOnName() {
        personRepository.findAll()
                .filter(person -> person.getFirstName().equals("Fiona"))
                .subscribe(person -> System.out.println(person.getFirstName()));
    }

    @Test
    void testGetById() {
        Mono<Person> fionaMono = personRepository.findAll()
                .filter(person -> person.getFirstName().equals("Fiona")).next();

        fionaMono.subscribe(person -> System.out.println(person.getFirstName()));
    }

    @Test
    void testFindPersonByIdNotFound() {
        Flux<Person> personFlux = personRepository.findAll();

        final Integer id = 8;

        Mono<Person> personMono = personFlux.filter(person -> person.getId().equals(id)).single()
                .doOnError(throwable -> {
                    System.out.println("Error occurred in flux");
                    System.out.println(throwable.toString());
                });

        personMono.subscribe(System.out::println, throwable -> {
            System.out.println("Error occurred in the mono");
            System.out.println(throwable.toString());
        });
    }

    @Test
    void getByIdFound() {
        Mono<Person> personMono = personRepository.getById(3);

        assertEquals(Boolean.TRUE, personMono.hasElement().block());
    }

    @Test
    void getByIdFoundStepVerifier() {
        Mono<Person> personMono = personRepository.getById(3);

        StepVerifier.create(personMono).expectNextCount(1).verifyComplete();

        personMono.subscribe(person -> System.out.println(person.getFirstName()));
    }

    @Test
    void getByIdNotFound() {
        Mono<Person> personMono = personRepository.getById(10);

        assertEquals(Boolean.FALSE, personMono.hasElement().block());
    }

    @Test
    void getByIdNotFoundStepVerifier() {
        Mono<Person> personMono = personRepository.getById(10);

        StepVerifier.create(personMono).expectNextCount(0).verifyComplete();

        personMono.subscribe(person -> System.out.println(person.getFirstName()));
    }
}