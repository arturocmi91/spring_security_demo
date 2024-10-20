SELECT 
    u.user_name,
    r.role_name AS role_name,
    p.permission_enum AS permission_enum
FROM
    users u
        INNER JOIN
    user_role ur ON u.id = ur.user_id
        INNER JOIN
    roles r ON ur.role_id = r.id
        INNER JOIN
    role_permission rp ON r.id = rp.role_id
        INNER JOIN
    permissions p ON rp.permission_id = p.id;