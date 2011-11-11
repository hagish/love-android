-- overwrites the current pairs implementation to become invariant to removing elements during iteration
-- because of a bug in luaj

local _pairs = pairs
pairs = function(t)
	if t == nil then return _pairs(t) end
	
	-- generate a map to map each key to its successor
	local map_cur_key_to_next = {}
	local last_key = nil
	local fist_key = nil

	for k,v in _pairs(t) do 
		if last_key then
			map_cur_key_to_next[last_key] = k
		else
			first_key = k
		end

		last_key = k
	end
	
	-- empty table?
	if last_key == nil then return _pairs(t) end

	local local_next = function(t, index) 
		if index == nil then
			return first_key, t[first_key]
		elseif index == last_key or t == nil then
			return nil
		else
			local k = map_cur_key_to_next[index]
			return k, t[k]
		end
		
		return next(t, index) 
	end
	
	-- next,t,nil
	return local_next, t, nil
end
