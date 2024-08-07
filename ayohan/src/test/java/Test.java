import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class Test {
	public static void main(String[] args) {
		List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		List<CompletableFuture<Integer>> futures = nums.stream()
				.map(value -> CompletableFuture.supplyAsync(() -> {
					// 这里是每个异步任务要执行的操作，
					return value;
				}))
				.collect(Collectors.toList());

		CompletableFuture<Integer> sumFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
				.thenApplyAsync(v -> {
					// 所有异步计算任务完成后，将它们的结果进行合并
					int sum = futures.stream()
							.mapToInt(CompletableFuture::join)
							.sum();
					return sum;
				});

		int sum = sumFuture.join();
		System.out.println(sum);
	}
}