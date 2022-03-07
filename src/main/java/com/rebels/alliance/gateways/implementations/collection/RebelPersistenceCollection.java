package com.rebels.alliance.gateways.implementations.collection;

import com.rebels.alliance.domains.Rebel;
import com.rebels.alliance.gateways.RebelGateway;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Repository;

import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class RebelPersistenceCollection implements RebelGateway {

    static public List<Rebel> rebels = new ArrayList<>();
    private static long REBELID = 0;

    private static String[] getNullPropertyNames(Object source) {
        Class clazz;
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] propertyDescriptors = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : propertyDescriptors) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    @Override
    public Rebel register(Rebel rebel) {
        Rebel newRebel = rebel;
        newRebel.setId(REBELID++);
        newRebel.getInventory().setOwnerId(newRebel.getId());
        rebels.add(newRebel);
        return newRebel;
    }

    @Override
    public void delete(Rebel rebel) {
        rebels = rebels.stream()
                .filter(rb -> rb.getId() != rebel.getId())
                .collect(Collectors.toList());
    }

    @Override
    public List<Rebel> findAll() {
        return rebels;
    }

    @Override
    public <V> List<Rebel> findByParam(String parameter, V value) {
        List<Rebel> matches = new ArrayList<>();
        String lowerCased = parameter.toLowerCase();

        if (lowerCased.equals("id")) {
            matches.addAll(rebels.stream()
                    .filter(r -> r.getId() == Long.parseLong(String.valueOf(value)))
                    .collect(Collectors.toList()));
        }

        if (lowerCased.equals("name")) {
            matches.addAll(rebels.stream()
                    .filter(r -> Objects.equals(r.getName(), String.valueOf(value)))
                    .collect(Collectors.toList()));
        }
        if (lowerCased.equals("age")) {
            matches.addAll(rebels.stream()
                    .filter(r -> r.getAge() == Integer.parseInt(String.valueOf(value)))
                    .collect(Collectors.toList()));
        }
        if (lowerCased.equals("gender")) {
            matches.addAll(rebels.stream()
                    .filter(r -> Objects.equals(String.valueOf(r.getGender()), String.valueOf(value)))
                    .collect(Collectors.toList()));
        }
        if (lowerCased.equals("location")) {
            matches.addAll(rebels.stream()
                    .filter(r -> Objects.equals(r.getLocation().getGalaxyName(), String.valueOf(value)))
                    .collect(Collectors.toList()));
        }

        return matches;
    }

    @Override
    public Rebel updateRebel(Rebel rebel) {
        for (Rebel rb : rebels) {
            if (Objects.equals(rb.getId(), rebel.getId())) {
                BeanUtils.copyProperties(rebel, rb, getNullPropertyNames(rebel));
                return rb;
            }
        }
        return null;
    }
}