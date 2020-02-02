package com.example.perfect.service;

import com.example.perfect.exception.InvalidRequestException;
import com.example.perfect.model.PerfectNumbers;
import com.google.common.math.LongMath;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.google.common.math.LongMath.checkedPow;
import static com.google.common.math.LongMath.isPrime;


@Service
public class PerfectService {

    public Mono<Boolean> findPerfect(long number) {
        return Mono.fromCallable(() -> findAllPerfectsInRange(number, number))
                .flatMap(l -> Mono.just(l.contains(number)));
    }

    public Mono<PerfectNumbers> findAllPerfects(long start, long end) {
        return Mono.fromCallable(() -> findAllPerfectsInRange(start, end))
                .map(perfectNums -> new PerfectNumbers(perfectNums));
    }

    private List<Long> findAllPerfectsInRange(long start, long end) {

        if (start > end) {
            throw new InvalidRequestException("Invalid range. Start should be less than end");
        }

        int nStart = LongMath.log2(start, RoundingMode.DOWN);
        int nEnd = LongMath.log2(end, RoundingMode.DOWN);

        int lowerPrime = (nStart / 2) < 2 ? 2 : nStart / 2;
        int upperPrime = nEnd / 2 + 1;

        return IntStream.rangeClosed(lowerPrime, upperPrime)
                .boxed()
                .filter(number -> isMersennePrime(number))
                .map(this::calculatePerfectNum)
                .filter(value -> value >= start)
                .dropWhile(value -> value > end)
                .collect(Collectors.toUnmodifiableList());

    }

    private boolean isMersennePrime(int number) {
        if(!isPrime(number))
            return false;

        return isPrime(checkedPow(2, number) - 1);
    }

    private long calculatePerfectNum(int p) {
        long partB = checkedPow(2, p);
        long partA = partB >> 1;
        BigInteger result = BigInteger.valueOf(partA).multiply(BigInteger.valueOf(partB - 1));
        return result.longValue();
    }

}
