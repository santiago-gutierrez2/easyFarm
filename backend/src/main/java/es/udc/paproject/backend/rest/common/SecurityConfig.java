package es.udc.paproject.backend.rest.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private JwtGenerator jwtGenerator;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.cors().and().csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.addFilter(new JwtFilter(authenticationManager(), jwtGenerator))
			.authorizeRequests()
			.antMatchers(HttpMethod.POST, "/users/signUp").permitAll()
			.antMatchers(HttpMethod.POST, "/users/login").permitAll()
			.antMatchers(HttpMethod.POST, "/users/loginFromServiceToken").permitAll()
			.antMatchers(HttpMethod.POST, "/users/createEmployee").hasRole("ADMIN")
			.antMatchers(HttpMethod.PUT, "/users/*").hasAnyRole("ADMIN", "EMPLOYEE")
			.antMatchers(HttpMethod.POST, "/users/*/changePassword").hasAnyRole("ADMIN","EMPLOYEE")
			.antMatchers(HttpMethod.GET, "/users/getEmployees").hasAnyRole("ADMIN")
			.antMatchers(HttpMethod.DELETE, "/users/*/deleteEmployee").hasAnyRole("ADMIN")
			.antMatchers(HttpMethod.POST, "/issues/createIssue").hasAnyRole("ADMIN")
			.antMatchers(HttpMethod.PUT, "/issues/*").hasAnyRole("ADMIN","EMPLOYEE")
			.antMatchers(HttpMethod.GET, "/issues/*").hasAnyRole("EMPLOYEE","ADMIN")
			.antMatchers(HttpMethod.PUT, "/issues/*/setAsDone").hasAnyRole("EMPLOYEE", "ADMIN")
			.antMatchers(HttpMethod.GET, "/issues/allIssues").hasAnyRole("ADMIN")
			.antMatchers(HttpMethod.DELETE, "/issues/*/deleteIssue").hasAnyRole("ADMIN")
			.antMatchers(HttpMethod.POST, "/foodPurchase/createFoodPurchase").hasAnyRole("ADMIN", "EMPLOYEE")
			.antMatchers(HttpMethod.PUT, "/foodPurchase/*").hasAnyRole("ADMIN", "EMPLOYEE")
			.antMatchers(HttpMethod.GET, "/foodPurchase/*").hasAnyRole("ADMIN", "EMPLOYEE")
			.antMatchers(HttpMethod.GET, "/foodPurchase/allFoodPurchases").hasAnyRole("ADMIN", "EMPLOYEE").antMatchers(HttpMethod.GET, "/foodPurchase/getAvailableFoodBatches").hasAnyRole("ADMIN", "EMPLOYEE")
			.antMatchers(HttpMethod.DELETE, "/foodPurchase/*/deleteFoodPurchase").hasAnyRole("ADMIN", "EMPLOYEE")
			.antMatchers(HttpMethod.POST, "/animal/registerAnimal").hasAnyRole("ADMIN", "EMPLOYEE")
			.antMatchers(HttpMethod.PUT, "/animal/*").hasAnyRole("ADMIN", "EMPLOYEE")
			.antMatchers(HttpMethod.DELETE, "/animal/*/isDead").hasAnyRole("ADMIN", "EMPLOYEE")
			.antMatchers(HttpMethod.GET, "/animal/*").hasAnyRole("ADMIN", "EMPLOYEE")
			.antMatchers(HttpMethod.GET, "/animal/allAnimals").hasAnyRole("ADMIN", "EMPLOYEE")
			.antMatchers(HttpMethod.GET, "/animal/animalsWithLabel").hasAnyRole("ADMIN", "EMPLOYEE")
			.antMatchers(HttpMethod.GET, "/animal/animalFoodConsumptionChart/*").hasAnyRole("ADMIN", "EMPLOYEE")
			.antMatchers(HttpMethod.POST, "/foodConsumption/registerFoodConsumption").hasAnyRole("ADMIN", "EMPLOYEE")
			.antMatchers(HttpMethod.POST, "/foodConsumption/registerSeveralFoodConsumptions").hasAnyRole("ADMIN", "EMPLOYEE")
			.antMatchers(HttpMethod.PUT, "/foodConsumption/*").hasAnyRole("ADMIN", "EMPLOYEE")
			.antMatchers(HttpMethod.DELETE, "/foodConsumption/*").hasAnyRole("ADMIN", "EMPLOYEE")
			.antMatchers(HttpMethod.GET, "/foodConsumption/*").hasAnyRole("ADMIN", "EMPLOYEE")
			.antMatchers(HttpMethod.GET, "/foodConsumption/allFoodConsumptions").hasAnyRole("ADMIN", "EMPLOYEE")
			.antMatchers(HttpMethod.GET, "/foodConsumption/stockChart").hasAnyRole("ADMIN", "EMPLOYEE")
			.antMatchers(HttpMethod.GET, "/foodConsumption/consumptionChart/*").hasAnyRole("ADMIN", "EMPLOYEE")
			.antMatchers(HttpMethod.POST, "/weighing/registerWeighing").hasAnyRole("ADMIN", "EMPLOYEE")
			.antMatchers(HttpMethod.PUT, "/weighing/*").hasAnyRole("ADMIN", "EMPLOYEE")
			.antMatchers(HttpMethod.DELETE, "/weighing/*").hasAnyRole("ADMIN", "EMPLOYEE")
			.antMatchers(HttpMethod.GET, "/weighing/*").hasAnyRole("ADMIN", "EMPLOYEE")
			.antMatchers(HttpMethod.GET, "/weighing/AllWeighingByFarmId").hasAnyRole("ADMIN", "EMPLOYEE")
			.antMatchers(HttpMethod.GET, "/weighing/allWeighingByAnimalId/*").hasAnyRole("ADMIN", "EMPLOYEE")
			.antMatchers(HttpMethod.POST, "/milking/registerMilking").hasAnyRole("ADMIN", "EMPLOYEE")
			.antMatchers(HttpMethod.GET, "/milking/AllMilkingByFarmId").hasAnyRole("ADMIN", "EMPLOYEE")
			.antMatchers(HttpMethod.GET, "/milking/AllMilkingByFarmId").hasAnyRole("ADMIN", "EMPLOYEE")
			.antMatchers(HttpMethod.GET, "/milking/allMilkingByAnimalId/*").hasAnyRole("ADMIN", "EMPLOYEE")
			.antMatchers(HttpMethod.PUT, "/milking/*").hasAnyRole("ADMIN", "EMPLOYEE")
			.antMatchers(HttpMethod.GET, "/milking/*").hasAnyRole("ADMIN", "EMPLOYEE")
			.antMatchers(HttpMethod.DELETE, "/milking/*").hasAnyRole("ADMIN", "EMPLOYEE")
			.anyRequest().denyAll();

	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		
		CorsConfiguration config = new CorsConfiguration();
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		
		config.setAllowCredentials(true);
	    config.setAllowedOriginPatterns(Arrays.asList("*"));
	    config.addAllowedHeader("*");
	    config.addAllowedMethod("*");
	    
	    source.registerCorsConfiguration("/**", config);
	    
	    return source;
	    
	 }

}
