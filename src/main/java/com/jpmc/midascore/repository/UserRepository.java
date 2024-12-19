package com.jpmc.midascore.repository;

import com.jpmc.midascore.entity.UserRecord;
import io.micrometer.common.lang.NonNull;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserRecord, Long> {

    UserRecord findById(long id);


    @Query("SELECT u.balance FROM UserRecord u where u.id = ?1")
    float getBalanceById(long id);

    @Transactional
    @Modifying
    @Query("update UserRecord u set u.balance = ?2 where u.id = ?1")
    void updateBalanceById(long id, float balance);

    UserRecord findByName(@NonNull String name);


}
